package fr.uge.tropico;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class GameVS {
    private static GameVS actualGame = null;

    private ArrayList<GamePlayer> playersGames = new ArrayList<>();
    private int gameSize;
    private String vsName;
    private GamePlayer actualPlayer;
    private ArrayList<GamePlayer> toRemove = new ArrayList<>();

    /**
     * Create a multiplayer game
     * @param scenario
     * @param difficulty
     * @param vsName
     * @param playersNames
     */
    public GameVS(Scenario scenario, Difficulty difficulty, String vsName, ArrayList<String> playersNames) {
        this.vsName = vsName;
        playersNames.forEach(pn -> playersGames.add(new GamePlayer(scenario, difficulty, vsName, pn)));
        actualPlayer = playersGames.get(0);
        this.gameSize = playersGames.size();
    }

    /**
     * Is only used for GSON, don't use this please for the love of god
     * @param playersGames
     * @param gameSize
     * @param vsName
     * @param actualPlayer
     * @param toRemove
     */
    public GameVS(ArrayList<GamePlayer> playersGames, int gameSize, String vsName, GamePlayer actualPlayer, ArrayList<GamePlayer> toRemove) {
        this.playersGames = playersGames;
        this.gameSize = gameSize;
        this.vsName = vsName;
        this.actualPlayer = actualPlayer;
        this.toRemove = toRemove;
    }

    /**
     * A game for a player, extends Game so that we can reuse it inside of GameVS while changing some methods.
     */
    public class GamePlayer extends Game {

        private String playerName;

        /**
         * @param scenario
         * @param difficulty
         * @param gameName
         * @param playerName
         */
        public GamePlayer(Scenario scenario, Difficulty difficulty, String gameName, String playerName) {
            super(scenario, difficulty, gameName);
            this.playerName = playerName;
        }

        /**
         * Let a player play a sequence of turns
         * @throws IOException
         */
        @Override
        public void run() throws IOException {
            if (eventsQueue.peek() != null){
                actualEvent.addAll(eventsQueue.poll());
            }
            else {
                eventsQueue.poll();
            }
            turn(seasons[score%4], actualEvent);
            if(isDown()) {
                String[] seasonName = {"d'Hiver","de Printemps","d'Eté","d'Automne"};
                if(loseCondition == 2) {
                    System.out.println("Votre pays a fait faillite par cette belle journée " + seasonName[score%4] + " et vous n'avez plus personne à diriger.\nVous êtes resté " + (int) (score/4) + " année(s) au pouvoir. Félicitations El Ex-Presidente.");
                }
                System.out.println("Le peuple s'est insurgé et vous avez été renversé par cette belle journée " + seasonName[score%4] + ".\nVous êtes resté " + (int) (score/4) + " année(s) au pouvoir. Félicitations El Ex-Presidente.");
            }
            else{
                score++;
                if (score%4==0){
                    Main.skipWin();
                    endYear();
                    if (isDown()){
                        String[] seasonName = {"d'Hiver","de Printemps","d'Eté","d'Automne"};
                        if(loseCondition == 2) {
                            System.out.println("Votre pays a fait faillite par cette belle journée " + seasonName[score%4] + " et vous n'avez plus personne à diriger.\nVous êtes resté " + (int) (score/4) + " année(s) au pouvoir. Félicitations El Ex-Presidente.");
                        }
                        System.out.println("Le peuple s'est insurgé et vous avez été renversé par cette belle journée " + seasonName[score%4] + ".\nVous êtes resté " + (int) (score/4) + " année(s) au pouvoir. Félicitations El Ex-Presidente.");
                    }
                }
            }
        }

        /**
         * Same as Game.turn(), but it also send multiplayers effects
         * @param season
         * @param eventPassed
         * @throws IOException
         */
        @Override
        public void turn(String season, Event eventPassed) throws IOException {
            super.turn(season, eventPassed);
            sendEventsAndEffects(season, actualGame.playersGames);
            actualAnswers.clear();
        }

        /**
         * Same as Game.turn(String, ArrayList<Event>) but it adds a string on top of it so say which player must play.
         * @param season
         * @param eventsTurn
         * @throws IOException
         */
        @Override
        public void turn(String season, ArrayList<Event> eventsTurn) throws IOException {
            Main.skipWin("C'est au tour du Presidente " + playerName + " !");

            if (eventsTurn == null || eventsTurn.isEmpty()){
                turn(season);
                actualEvent.remove(0);
            }
            else{
                for (Event e :
                        (ArrayList<Event>) eventsTurn.clone()) {
                    turn(season,e);
                    actualEvent.remove(0);
                }
            }
        }

        /**
         * Save the whole GameVS
         * @throws IOException
         */
        @Override
        public void saveGame() throws IOException {
            actualGame.saveGameVS();
        }

        /**
         * Send effects to other players in ArrayList<GamePlayer> enemies
         * @param season
         * @param enemies
         */
        private void sendEventsAndEffects(String season, ArrayList<GamePlayer> enemies){
            for (GamePlayer enemy :
                    enemies) {
                if (enemy.equals(this)) {
                    continue;
                }

                enemy.applyVsAnswers(season, this.actualAnswers);

            }
        }

        /**
         * Apply effects and queue events in other players' game from a list of answers
         * @param season
         * @param answers
         */
        public void applyVsAnswers(String season, ArrayList<Answer> answers){
            answers.forEach(a -> {
                try {
                    this.applyVSAnswer(season, a);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        /**
         * Apply effects and queue events in other players' game from an answer
         * @param season
         * @param answer
         * @throws IOException
         */
        public void applyVSAnswer(String season, Answer answer) throws IOException {
            HashMap<String, HashMap<String, Integer>> effects = answer.getVSEffects(); //application effets

            if (effects.containsKey("global")) {
                HashMap<String, Integer> global = effects.get("global"); //ajout de trésorerie
                if (global.containsKey("treasury")) {
                    treasury += global.get("treasury");
                } //ajout de trésorerie

                if (global.containsKey("industry")) {
                    int tmp = global.get("industry");
                    if (industry + tmp < 0) {
                        industry = 0;
                    } //mise à 0 dans le cas ou l'industrie serait négative
                    else if (industry + agriculture + tmp > 100) { //si le total industrie + agriculture est supérieur à 100, on réadapte les proportions obtenues pour atteindre un total de 100
                        industry = ((industry + tmp) / (industry + tmp + agriculture)) * 100;
                        agriculture = 100 - industry;
                    } else
                        industry += tmp;
                }

                if (global.containsKey("agriculture")) {
                    int tmp2 = global.get("agriculture");
                    if (agriculture + tmp2 < 0) {
                        agriculture = 0;
                    } else if (industry + agriculture + tmp2 > 100) {
                        agriculture = ((agriculture + tmp2) / (industry + tmp2 + agriculture)) * 100;
                        industry = 100 - agriculture;
                    } else
                        agriculture += tmp2;
                }
            }

            if (effects.containsKey("satisfaction")) { // apply satisfaction effects
                for (Faction f : factions) {
                    if (effects.get("satisfaction").keySet().contains(f.getFactionName())) {
                        f.changeSatisfaction(effects.get("satisfaction").get(f.getFactionName()));
                    }
                }
            }

            if (effects.containsKey("supporter")) { // apply supporter effects
                for (Faction f : factions) {
                    if (effects.get("supporter").keySet().contains(f.getFactionName())) {
                        f.changeSupporter(effects.get("supporter").get(f.getFactionName()));
                    }
                }
            }

            if (!answer.getVSevents().isEmpty()){ // will apply events (queue them)
                ArrayList<Event> answerEvents = (ArrayList) answer.getVSevents();
                HashMap<Integer,ArrayList<Event>> eventByWhen = new HashMap<>();
                int maxWhen = 0;
                for (Event e : answerEvents) {
                    if (e.getWhen() == 0){
                        Main.skipWin();
                        turn(season,e);
                        continue;
                    }
                    if (!eventByWhen.containsKey(e.getWhen())){
                        eventByWhen.put(e.getWhen(), new ArrayList<>());
                    }
                    eventByWhen.get(e.getWhen()).add(e);
                    if (maxWhen < e.getWhen()){
                        maxWhen = e.getWhen();
                    }
                }
                for (int i = 0; i <= maxWhen; i++) {
                    if (eventsQueue.size() > i){
                        if (eventByWhen.containsKey(i)) {
                            if (eventsQueue.get(i) == null) {
                                eventsQueue.set(i, new ArrayList<>());
                            }
                            eventsQueue.get(i).addAll(eventByWhen.get(i));
                        }
                    }
                    else {
                        if (eventByWhen.containsKey(i)) {
                            eventsQueue.offer(eventByWhen.get(i));
                        } else eventsQueue.offer(null);
                    }
                }
            }
        }
    }

    /**
     * Save the GameVS
     * @throws IOException
     */
    public void saveGameVS() throws IOException {
        try (var r = new OutputStreamWriter(new FileOutputStream("save/" + vsName + "___VS.json"), "UTF-8")) {
            new Gson().toJson(this, r);
        }
    }

    /**
     * Restore a GameVS from source (src)
     * @param src
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static GameVS restoreGameVS(String src) throws IOException, ClassNotFoundException {
        try (var r = new InputStreamReader(new FileInputStream(src), "UTF-8")) {
            return new Gson().fromJson(r, GameVS.class);
        }
    }

    /**
     * @return 1 if all players are down
     * @return 0 if at least one player survived
     * @return -1 if else
     */
    private int allDown(){
        int result = 0;
        for (GamePlayer player:
             playersGames) {
            if (!player.isDown()){
                result++;
            }
        }
        if (result == 1){
            return 0;
        }
        if (result == 0){
            return 1;
        }
        else{
            return -1;
        }
    }

    /**
     * Run the GameVS
     * @throws IOException
     */
    public void run() throws IOException {
        actualGame = this;
        if (actualPlayer != null && !actualPlayer.actualEvent.isEmpty()){
            if (!toRemove.isEmpty()){
                playersGames.removeAll(toRemove);
            }
            actualPlayer.turn(actualPlayer.seasons[actualPlayer.score%4], actualPlayer.actualEvent);
            if(!actualPlayer.isDown()) {
                actualPlayer.score++;
                if (actualPlayer.score%4==0){
                    Main.skipWin();
                    actualPlayer.endYear();
                }
                if (actualPlayer.isDown()) {
                    String[] seasonName = {"d'Hiver", "de Printemps", "d'Eté", "d'Automne"};
                    if (actualPlayer.loseCondition == 2) {
                        System.out.println("Votre pays a fait faillite par cette belle journée " + seasonName[actualPlayer.score % 4] + " et vous n'avez plus personne à diriger.\nVous êtes resté " + (int) (actualPlayer.score / 4) + " année(s) au pouvoir. Félicitations El Ex-Presidente.");
                    }
                    System.out.println("Le peuple s'est insurgé et vous avez été renversé par cette belle journée " + seasonName[actualPlayer.score % 4] + ".\nVous êtes resté " + (int) (actualPlayer.score / 4) + " année(s) au pouvoir. Félicitations El Ex-Presidente.");
                }
            }
            else {
                String[] seasonName = {"d'Hiver","de Printemps","d'Eté","d'Automne"};
                if(actualPlayer.loseCondition == 2) {
                    System.out.println("Votre pays a fait faillite par cette belle journée " + seasonName[actualPlayer.score%4] + " et vous n'avez plus personne à diriger.\nVous êtes resté " + (int) (actualPlayer.score/4) + " année(s) au pouvoir. Félicitations El Ex-Presidente.");
                }
                System.out.println("Le peuple s'est insurgé et vous avez été renversé par cette belle journée " + seasonName[actualPlayer.score%4] + ".\nVous êtes resté " + (int) (actualPlayer.score/4) + " année(s) au pouvoir. Félicitations El Ex-Presidente.");
            }
        }

        while(allDown() == -1 && !playersGames.isEmpty()) {
            toRemove.clear();
            for (GamePlayer player : playersGames) {
                actualPlayer = player;
                player.run();
                if (player.isDown()){
                    toRemove.add(player);
                }
            }
            playersGames.removeAll(toRemove);

        }

        StringBuilder sb = new StringBuilder();

        if (!playersGames.isEmpty()){
            sb.append("Félicitation Prsidente ");
            sb.append(playersGames.get(0).playerName);
            sb.append("!\nVous êtes vraiment El Presidente, le seul et l'unique!");
        }
        else{
            sb.append("Il semblerait que le monde soit en paix, il n'y a plus aucune dictature!\n\nFin.");
        }
        System.out.println(playersGames.size());
        System.out.println(sb.toString());
        System.out.println("Appuyez sur la touche entrée...");
        GlobalScanner sc = GlobalScanner.SYSTEM_IN;
        sc.nextLine();
    }

}
