package fr.uge.tropico;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.Serializable;
import java.util.*;

public class Game {

    static String[] seasons = {"WINTER","SPRING","SUMMER","AUTUMN"};
    private Scenario scenario;
    private Difficulty difficulty;
    private String gameName;
    int score;
    ArrayList<Faction> factions = new ArrayList<Faction>();
    int treasury;
    int industry;
    int agriculture;
    private int food;
    private int citizens;
    LinkedList<ArrayList<Event>> eventsQueue = new LinkedList<ArrayList<Event>>();
    int loseCondition;
    ArrayList<Event> actualEvent = new ArrayList<>();
    ArrayList<Answer> actualAnswers = new ArrayList<>();

    public Game(Scenario scenario, Difficulty difficulty, String gameName) {
        this.scenario = scenario;
        this.difficulty = difficulty;
        this.factions = (ArrayList) scenario.getFactions();
        for (Faction f : factions) { //calcul population initiale
            citizens += f.getSupporter();
        }
        food = difficulty.getFood();
        treasury = difficulty.getTreasury();
        agriculture = difficulty.getAgriculture();
        industry = difficulty.getIndustry();
        this.gameName = gameName;

    }

    public Game(Scenario scenario, Difficulty difficulty, String gameName, int score, ArrayList<Faction> factions, int treasury, int industry, int agriculture, int food, int citizens, LinkedList<ArrayList<Event>> eventsQueue, int loseCondition, ArrayList<Event> actualEvent, ArrayList<Answer> actualAnswers) {
        this.scenario = scenario;
        this.difficulty = difficulty;
        this.gameName = gameName;
        this.score = score;
        this.factions = factions;
        this.treasury = treasury;
        this.industry = industry;
        this.agriculture = agriculture;
        this.food = food;
        this.citizens = citizens;
        this.eventsQueue = eventsQueue;
        this.loseCondition = loseCondition;
        this.actualEvent = actualEvent;
        this.actualAnswers = actualAnswers;
    }

    /**
     * @return global satifaction of a player's game
     */
    public int getGlobalSatisfaction() {
        int satisfaction = 0;
        int globalSupporters = 0;
        for (int i = 0; i < factions.size(); i++) {
            Faction faction = factions.get(i);
            satisfaction += faction.getSatisfaction()*faction.getSupporter();
            globalSupporters += faction.getSupporter();
        }
        return satisfaction/globalSupporters;
    }

    /**
     * Play a turn with a random event and in the season in parameter
     * @param season
     * @throws IOException
     */
    public void turn(String season) throws IOException {
        turn(season, (Event) null);
    }

    /**
     * play a turn with events in the list of events (eventsTurn) and in the season in parameter
     * @param season
     * @param eventsTurn
     * @throws IOException
     */
    public void turn(String season, ArrayList<Event> eventsTurn) throws IOException {
        Main.skipWin();

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
     * Play a turn with the event in parameter and in the season in parameter
     * @param season
     * @param eventPassed
     * @throws IOException
     */
    public void turn(String season, Event eventPassed) throws IOException {
        GlobalScanner scan = GlobalScanner.SYSTEM_IN;
        Event event;
        if (eventPassed == null){
            event = scenario.pickRandomEvent(season);
            actualEvent.add(event);
        }
        else{
            event = eventPassed;
        }
        // event = eventPassed == null ? scenario.pickRandomEvent(season) : eventPassed; //choix aléatoire de l'event si il n'y a pas d'évènement passé en paramètre
        saveGame();
        int choice;
        do {
            System.out.println("Entrez 0 pour afficher les stats.");
            System.out.println("Saison : " + season + " | Année : " + score/4);
            System.out.println(event.getTitle()); //affichage intitulé event
            for (int j = 0; j < event.getAnswers().size(); j++) {
                System.out.println((j + 1) + ") " + event.getAnswers().get(j).getName()); //affichage réponses
            }

            choice = scan.nextInt(); //choix réponse
            if (choice == 0) { Main.skipWin(showStats()); }

            else if (choice > 0 && choice <= event.getAnswers().size()) {
                Answer answer = event.getAnswers().get(choice - 1);

                applyAnswer(season, answer);
                actualAnswers.add(answer);

            }

            else
                System.out.println("Veuillez choisir un choix possible petit margoulin !");
        } while (choice <= 0 || choice > event.getAnswers().size());
    }

    /**
     * Apply effects of the answer in parameter
     * @param season
     * @param answer
     * @throws IOException
     */
    public void applyAnswer(String season, Answer answer) throws IOException {
        HashMap<String, HashMap<String, Integer>> effects = answer.getEffects(); //application effets

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

        if (effects.containsKey("satisfaction")) {
            for (Faction f : factions) {
                if (effects.get("satisfaction").keySet().contains(f.getFactionName())) {
                    f.changeSatisfaction(effects.get("satisfaction").get(f.getFactionName()));
                }
            }
        }

        if (effects.containsKey("supporter")) {
            for (Faction f : factions) {
                if (effects.get("supporter").keySet().contains(f.getFactionName())) {
                    f.changeSupporter(effects.get("supporter").get(f.getFactionName()));
                }
            }
        }

        if (!answer.getEvents().isEmpty()){
            ArrayList<Event> answerEvents = (ArrayList) answer.getEvents();
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

    /**
     * run the vent of the end of the year where we take a look at players statistics
     */
    void endYear() {
        GlobalScanner scan = GlobalScanner.SYSTEM_IN;
        System.out.println("Voici venu le temps du bilan de fin d'année.\n Avant que le bilan tombe, souhaitez vous prendre d'autres décisions?");
        int choice;
        do {
            System.out.println("0) Voir les statistiques\n"+
                    "1) Donner des pots de vin\n" +
                    "2) Acheter de la nourriture\n" +
                    "3) Passer au bilan.");
            choice = scan.nextInt();
            if (choice == 1) { corruption(); }
            else if (choice == 2) { market(); }
            else if(choice == 0){
                Main.skipWin(showStats());
            }
            else if (choice != 3){
                Main.skipWin("Veuillez choisir un choix possible petit margoulin !");
            }
        } while (choice != 3);
        Main.skipWin();
        treasury += 10 * industry;
        food += 40 * agriculture;
        System.out.println("Voici donc le bilan de l'année :");
        checkFood();
        System.out.print("Population : " + citizens + " habitants\n" +
                "Satisfaction globale : " + getGlobalSatisfaction() + " %\n" +
                "Trésorerie de l'Etat : " + treasury + " $\n" +
                "Réserves de nourriture : " + food +" unités\n" +
                "Part de l'agriculture : " + agriculture + "%\n" +
                "Part de l'industrie : " + industry + "%\n" +
                "Appuyez sur la touche entrée ...");
        scan.nextLine();
    }

    /**
     * Let the player corrupt a faction for the greater good
     */
    public void corruption() {
        GlobalScanner scan = GlobalScanner.SYSTEM_IN;
        Main.skipWin();
        int choice;
        Faction matchingFaction = factions.stream().filter(f -> f.getFactionName().equals("LOYALISTES")).findFirst().orElse(null);
        int loyalistIndex = matchingFaction == null ? -10 : factions.indexOf(matchingFaction);
        do {
            System.out.println("C'est l'heure de soudoyer un peu de monde.\n" +
                    "Quelle faction soudoyer?\n");
            System.out.println("-1) Voir les statistiques\n0) Personne, je suis honnête :)");
            for (int i = 0; i < factions.size(); i++) {
                    System.out.println((i+1) + ") " + factions.get(i).getFactionName());
            }
            choice = scan.nextInt();
            if (choice == 0) {
                Main.skipWin("C'est bien de voir un peu d'honnêteté.");
            }
            else if(choice == -1){
                Main.skipWin(showStats());
            }
            else if (choice > 0 && choice < factions.size()+1) {
                if (choice - 1 != loyalistIndex) {
                    int money = 15 * factions.get(choice - 1).getSupporter();
                    if (money <= treasury) {
                        treasury -= money;
                        factions.get(choice - 1).changeSatisfaction(10);
                        if (loyalistIndex != -10){
                            factions.get(loyalistIndex).changeSatisfaction(money/-10);
                        }
                        System.out.println("Vous venez de soudoyer les " + factions.get(choice - 1).getFactionName() + " ils vous le revaudront, au contraire des loyalistes.");
                    }
                    else
                        System.out.println("Malheureusement, vous n'avez pas les moyens de les soudoyer.");
                }
                else
                    System.out.println("Vous n'allez tout de même pas soudoyer vos loyalistes...");
            }
            else
                System.out.println("Veuillez choisir un choix possible petit margoulin !");
        } while(choice != 0);
    }

    /**
     * Let the player buy some food for his citizens
     */
    public void market() {
        GlobalScanner scan = GlobalScanner.SYSTEM_IN;
        Main.skipWin();
        int choice;
        do {
            System.out.println("Bienvenue au marché alimentaire, vous avez actuellement " + food + "unités de nourriture.\n" +
                    "Il vous vous reste " + treasury + " $.\n" +
                    "Le prix d'une unité est 8$. Combien voulez vous en acheter? (0 pour partir)");

            choice = scan.nextInt();
            if (choice == 0) {
                Main.skipWin("Au revoir...");
            }
            else if (choice == -1){
                Main.skipWin(showStats());
            }
            else if (choice > 0) {
                if ((choice * 8) <= treasury) {
                    food += choice;
                    treasury -= choice * 8;
                    System.out.println("Vous venez d'acheter " + choice + " unités de nourriture pour nourrire le bon peuple.");
                } else
                    System.out.println("Vous ne pouvez pas acheter tant de nourriture, vous êtes trop pauvre");
            }
            else if (choice < 0) {
                if (choice * -1 <= food) {
                    System.out.println("Vous venez de vendre " + choice * -1 + " unités de nourriture. Attention à ce qu'il en reste pour le peuple!");
                    food += choice;
                    treasury -= choice * 8;
                } else
                    System.out.println("On vous voit essayer de vendre plus de nourriture que vous en avez, on nous la fait pas.");
            }
        } while (choice != 0);
    }

    /**
     * check if the player as enough food for his citizens, if not then we alter his statistics
     */
    public void checkFood() {
        if (food < difficulty.getFoodNeeded() * citizens) {
            int murderNecesary = (citizens * difficulty.getFoodNeeded() - food)/difficulty.getFoodNeeded() + 1;

            // int tmpFood = food % difficulty.getFoodNeeded();
            System.out.println("Aïe! Coup dur pour Tropico!\n" +
                    "La nourriture manque et la population se meurt.\n" +
                    "Des gens meurent et le peuple est mécontent.");
            Random random = new Random();
            for (int i = 0; i < murderNecesary; i++) {
                factions.get(random.nextInt(factions.size())).changeSupporter(-1);
                citizens--;
                factions.forEach(f->f.changeSatisfaction(-2));
            }
            food = food - citizens * difficulty.getFoodNeeded();
        }
        else {
            System.out.println("Félicitations! La nourriture fut abondante et la population croit à vue d'oeil!");
            Random random = new Random();
            int populationGrowth = (citizens * ((random.nextInt(10) + 1)) / 100);
            for (int i = 0; i < populationGrowth; i++) {
                factions.get(random.nextInt(factions.size())).changeSupporter(1);
                citizens++;
            }
            food -= citizens * difficulty.getFoodNeeded();
        }
    }

    /**
     * @return if the player is down
     */
    public boolean isDown() {
        if (getGlobalSatisfaction() < difficulty.getEndAt()){
            loseCondition = 1;
            return true;
        }
        if (treasury <= 0){
            loseCondition = 2;
            return true;
        }
        return false;
    }

    /**
     * @return let the player see his actual statistics
     */
    public String showStats() {
        String result = "\n----------Statistiques de factions----------\n";
        for (Faction f : factions) {
            result += f.getFactionName() + " : " + f.getSupporter() + " partisans, " + f.getSatisfaction() + "% de satisfaction\n";
        }
        result += "-----------Statistiques globales------------\n" +
                "Population : " + citizens + " habitants\n" +
                "Satisfaction globale : " + getGlobalSatisfaction() + " %\n" +
                "Trésorerie de l'Etat : " + treasury + " $\n" +
                "Réserves de nourriture : " + food +" unités\n" +
                "Part de l'agriculture : " + agriculture + "%\n" +
                "Part de l'industrie : " + industry + "%\n" +
                "--------------------------------------------\n";
        return result;
    }

    /**
     * Save player's game
     * @throws IOException
     */
    public void saveGame() throws IOException {
        try (var r = new OutputStreamWriter(new FileOutputStream("save/" + this.gameName + "_SOLO.json"), "UTF-8")) {
            new Gson().toJson(this, r);
        }
    }

    /**
     * Can restore a game from a source (src)
     * @param src
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Game restoreGame(String src) throws IOException, ClassNotFoundException {
        try (var r = new InputStreamReader(new FileInputStream(src), "UTF-8")) {
            return new Gson().fromJson(r, Game.class);
        }
    }

    @Override
    public String toString() {
        return "Game{" +
                "scenario=" + scenario +
                ", difficulty=" + difficulty +
                ", gameName='" + gameName + '\'' +
                ", score=" + score +
                ", factions=" + factions +
                ", treasury=" + treasury +
                ", industry=" + industry +
                ", agriculture=" + agriculture +
                ", food=" + food +
                ", citizens=" + citizens +
                ", eventsQueue=" + eventsQueue +
                ", loseCondition=" + loseCondition +
                ", actualEvent=" + actualEvent +
                ", actualAnswers=" + actualAnswers +
                '}';
    }

    /**
     * Start the game
     * @throws IOException
     */
    public void run() throws IOException {
        if (!actualEvent.isEmpty()){
            turn(seasons[score%4], actualEvent);
            if(!isDown()) {score++;}
            if (score%4==0){
                Main.skipWin();
                endYear();
            }
        }
        while(!isDown()) {
            if (eventsQueue.peek() != null){
                actualEvent.addAll(eventsQueue.poll());
            }
            else {
                eventsQueue.poll();
            }
            turn(seasons[score%4], actualEvent);
            if(isDown()) {break;}
            score++;
            if (score%4==0){
                Main.skipWin();
                endYear();
            }
        }
        String[] seasonName = {"d'Hiver","de Printemps","d'Eté","d'Automne"};
        if(loseCondition == 2) {
            System.out.println("Votre pays a fait faillite par cette belle journée " + seasonName[score%4] + " et vous n'avez plus personne à diriger.\nVous êtes resté " + (int) (score/4) + " année(s) au pouvoir. Félicitations El Ex-Presidente.");
        }
        System.out.println("Le peuple s'est insurgé et vous avez été renversé par cette belle journée " + seasonName[score%4] + ".\nVous êtes resté " + (int) (score/4) + " année(s) au pouvoir. Félicitations El Ex-Presidente.");
        System.out.println("Appuyez sur la touche entrée...");
        GlobalScanner sc = GlobalScanner.SYSTEM_IN;
        sc.nextLine();
    }
}
