package fr.uge.tropico;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    /**
     * Skip the window by adding a sequence of newlines and add a string passed in parameters at the end
     * @param stringValue
     */
    public static void skipWin(String stringValue){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + stringValue);
    }

    /**
     * Skip the window by adding a sequence of newlines and add a string passed in parameters at the end and if ln is false then it won't let a newline pass at the end of
     * @param stringValue
     * @param ln
     */
    public static void skipWin(String stringValue, boolean ln){
        if (!ln){
            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + stringValue);
        }
        else skipWin(stringValue);
    }

    /**
     * Skip the window by adding a sequence of newlines
     */
    public static void skipWin(){
        skipWin("");
    }

    /**
     * Return string value of a path of a scenario
     * @return
     * @throws IOException
     */
    public static String getScenarioFromfolder() throws IOException {
        StringBuilder sb = new StringBuilder();
        GlobalScanner sc = GlobalScanner.SYSTEM_IN;

        sb.append("╔═════════════---\n");
        sb.append("║ Choissez un scénario :\n");

        HashMap<Integer, String> optToScenName = new HashMap<>();

        File repertoire = new File("scenario/");
        String liste[] = repertoire.list();

        if (liste != null) {
            for (int i = 0; i < liste.length; i++) {
                int no = optToScenName.size() + 1;
                optToScenName.put(no, liste[i]);
                sb.append("║    ");
                sb.append(no);
                sb.append(". ");
                sb.append(liste[i].substring(0, liste[i].length() - 5));
                sb.append("\n");
            }
        } else {
            System.err.println("Nom de repertoire invalide");
        }


        sb.append("╠════════---  \n");
        sb.append("║ Réponse : ");
            /*Game game = new Game();
            game.run();*/

        System.out.print(sb.toString());

        int choiceScen = sc.nextInt();

        while (choiceScen > optToScenName.size() || choiceScen <= 0){
            System.out.print("║ \tRéponse non acceptée.\n║\n║ Réponse : ");
            choiceScen = sc.nextInt();
        }

        System.out.println();

        return "scenario/" + optToScenName.get(choiceScen);
    }

    /**
     * @return string value of a path of a save
     * @throws IOException
     */
    public static String getSaveFromfolder() throws IOException {
        StringBuilder sb = new StringBuilder();
        GlobalScanner sc = GlobalScanner.SYSTEM_IN;

        sb.append("╔═════════════---\n");
        sb.append("║ Choissez une sauvegarde :\n");

        HashMap<Integer, String> optToSaveName = new HashMap<>();

        File repertoire = new File("save/");
        String liste[] = repertoire.list();
        System.out.println(liste);

        if (liste != null) {
            for (int i = 0; i < liste.length; i++) {
                int no = optToSaveName.size() + 1;
                optToSaveName.put(no, liste[i]);
                sb.append("║    ");
                sb.append(no);
                sb.append(". ");
                if (liste[i].substring(liste[i].length() - 7, liste[i].length() - 5).equals("VS")){
                    sb.append("[VS]   : ");
                }
                else {
                    sb.append("[Solo] : ");
                }
                sb.append(liste[i].substring(0, liste[i].length() - 10));
                sb.append("\n");
            }
        } else {
            System.err.println("Nom de repertoire invalide");
        }


        sb.append("╠════════---  \n");
        sb.append("║ Réponse : ");
            /*Game game = new Game();
            game.run();*/

        skipWin(sb.toString(), false);

        int choiceScen = sc.nextInt();

        while (choiceScen > optToSaveName.size() || choiceScen <= 0){
            System.out.print("║ \tRéponse non acceptée.\n║\n║ Réponse : ");
            choiceScen = sc.nextInt();
        }

        return "save/" + optToSaveName.get(choiceScen);
    }

    /**
     * @return a string value of a path of a difficulty
     * @throws IOException
     */
    public static String getDifficultyFromFolder() throws IOException {
        StringBuilder sb = new StringBuilder();
        GlobalScanner sc = GlobalScanner.SYSTEM_IN;

        sb.append("╔═════════════---\n");
        sb.append("║ Choisissez une difficulté :\n");

        HashMap<Integer, String> optToDiffName = new HashMap<>();

        File repertoire = new File("difficulty/");
        String liste[] = repertoire.list();

        if (liste != null) {
            for (int i = 0; i < liste.length; i++) {
                int no = optToDiffName.size() + 1;
                optToDiffName.put(no, liste[i]);
                sb.append("║    ");
                sb.append(no);
                sb.append(". ");
                sb.append(liste[i].substring(0, liste[i].length() - 5));
                sb.append("\n");
            }
        } else {
            System.err.println("\n║ Nom de repertoire invalide");
        }

        sb.append("╠════════---  \n");
        sb.append("║ Réponse : ");
            /*Game game = new Game();
            game.run();*/

        skipWin(sb.toString(), false);

        int choiceDiff = sc.nextInt();

        while (choiceDiff > optToDiffName.size() || choiceDiff <= 0){
            System.out.print("║ \tRéponse non acceptée.\n║\n║ Réponse : ");
            choiceDiff = sc.nextInt();
        }

        return "difficulty/" + optToDiffName.get(choiceDiff);

    }

    /**
     * Is used only to return a Scenario and difficulty in main method
     */
    private static class Options {

        private String scenarioName;
        private String difficultyName;
        private String trueScenarioName;
        private String trueDifficultyName;
        private Scenario scenario;
        private Difficulty difficulty;

        private Options(){}

        /**
         * @return Options of a scenario and a difficulty
         * @throws IOException
         */
        public static Options chooseOptions() throws IOException {
            int choiceGame = 2;
            GlobalScanner sc = GlobalScanner.SYSTEM_IN;

            Options pair = new Options();

            while (choiceGame == 2) {
                skipWin();

                StringBuilder sb = new StringBuilder();

                File repertoire = new File("scenario/");
                String liste[] = repertoire.list();
                if (liste == null || liste.length == 0){
                    sb.append("╔═════════════---\n");
                    sb.append("║ Il n'y a pas de scénarios disponibles.\n");
                    sb.append("╠════════---  \n");
                    sb.append("║ Appuyez sur entrée ...");
                    skipWin(sb.toString(), false);
                    sc.nextLine();
                    System.exit(0);
                }

                pair.scenarioName = getScenarioFromfolder();
                pair.trueScenarioName = pair.scenarioName.substring("scenario/".length(),pair.scenarioName.length()-4);

                repertoire = new File("difficulty/");
                String liste2[] = repertoire.list();
                if (liste2 == null || liste2.length == 0){
                    sb.append("╔═════════════---\n");
                    sb.append("║ Il n'y a pas de difficultés disponibles.\n");
                    sb.append("╠════════---  \n");
                    sb.append("║ Appuyez sur entrée ...");
                    skipWin(sb.toString(), false);
                    sc.nextLine();
                    System.exit(0);
                }

                pair.difficultyName = getDifficultyFromFolder();
                pair.trueDifficultyName = pair.difficultyName.substring("difficulty/".length(), pair.difficultyName.length()-4);

                sb = new StringBuilder();

                sb.append("╔═════════════---\n");
                sb.append("║ 1. Lancer la partie ?\n");
                sb.append("║ 2. Changer les paramètres\n");
                sb.append("║    Scénario :\n");
                sb.append("║        | ");
                sb.append(pair.trueScenarioName);
                sb.append("\n║    Difficulté :\n");
                sb.append("║        | ");
                sb.append(pair.trueDifficultyName);
                sb.append("\n╠════════---  \n");
                sb.append("║ Réponse : ");
                skipWin(sb.toString(), false);

                choiceGame = sc.nextInt();

                while (choiceGame > 2 || choiceGame < 1) {
                    System.out.print("║ \tRéponse non acceptée.\n║\n║ Réponse : ");
                    choiceGame = sc.nextInt();
                }

            }

            pair.scenario = Scenario.loadFromResource(pair.scenarioName);
            pair.difficulty = Difficulty.loadFromResource(pair.difficultyName);

            return pair;
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        StringBuilder sb = new StringBuilder();
        GlobalScanner sc = GlobalScanner.SYSTEM_IN;
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                "║                                                                                                               ║\n" +
                "║  $$$$$$$$\\                            $$\\                            $$$$$$\\   $$$$$$\\  $$$$$$$$\\ $$$$$$$$\\   ║\n" +
                "║  \\__$$  __|                           \\__|                          $$  __$$\\ $$$ __$$\\ \\____$$  |\\____$$  |  ║\n" +
                "║     $$ | $$$$$$\\   $$$$$$\\   $$$$$$\\  $$\\  $$$$$$$\\  $$$$$$\\        \\__/  $$ |$$$$\\ $$ |    $$  /     $$  /   ║\n" +
                "║     $$ |$$  __$$\\ $$  __$$\\ $$  __$$\\ $$ |$$  _____|$$  __$$\\        $$$$$$  |$$\\$$\\$$ |   $$  /     $$  /    ║\n" +
                "║     $$ |$$ |  \\__|$$ /  $$ |$$ /  $$ |$$ |$$ /      $$ /  $$ |      $$  ____/ $$ \\$$$$ |  $$  /     $$  /     ║\n" +
                "║     $$ |$$ |      $$ |  $$ |$$ |  $$ |$$ |$$ |      $$ |  $$ |      $$ |      $$ |\\$$$ | $$  /     $$  /      ║\n" +
                "║     $$ |$$ |      \\$$$$$$  |$$$$$$$  |$$ |\\$$$$$$$\\ \\$$$$$$  |      $$$$$$$$\\ \\$$$$$$  /$$  /     $$  /       ║\n" +
                "║     \\__|\\__|       \\______/ $$  ____/ \\__| \\_______| \\______/       \\________| \\______/ \\__/      \\__/        ║\n" +
                "║                             $$ |                                                                              ║\n" +
                "║                             $$ |                                                                              ║\n" +
                "║                             \\__|                                                                              ║\n" +
                "║                                                                                                               ║\n" +
                "╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n\nAppuyez sur la touche entrée...");
        sc.nextLine();

        sb.append("╔═════════════---\n");
        sb.append("║ Que voulez vous faire ?\n");
        sb.append("║    1. Jouer une partie\n");
        sb.append("║    2. Jouer une partie VS\n");
        sb.append("║    3. Charger une sauvegarde\n");
        sb.append("║    0. Quitter\n");
        sb.append("╠════════---  \n");
        sb.append("║ Réponse : ");
        skipWin(sb.toString(), false);
        int choice = sc.nextInt();

        while (choice > 3 || choice < 0){
            System.out.print("║ Réponse non acceptée.\n║\n║ Réponse : ");
            choice = sc.nextInt();
        }

        sb = new StringBuilder();

        if (choice == 1){

            Options options = Options.chooseOptions();

            sb.append("╔═════════════---\n");
            sb.append("║ Quel nom donner à cette partie ?\n");
            sb.append("╠════════---  \n");
            sb.append("║ Réponse : ");
            skipWin(sb.toString(), false);

            String gameName = sc.nextLine();
            while (gameName.length() < 3){
                System.out.print("║ \tRéponse non acceptée. Il faut plus de 3 caractères.\n║ Réponse : ");
                gameName = sc.nextLine();
            }

            Game game = new Game(options.scenario, options.difficulty, gameName);
            game.run();

        }
        else if (choice == 2){

            Options options = Options.chooseOptions();

            sb.append("╔═════════════---\n");
            sb.append("║ Quel nom donner à cette partie ?\n");
            sb.append("╠════════---  \n");
            sb.append("║ Réponse : ");
            skipWin(sb.toString(), false);

            String gameName = sc.nextLine();
            while (gameName.length() < 3){
                System.out.print("║ \tRéponse non acceptée. Il faut plus de 3 caractères.\n║ Réponse : ");
                gameName = sc.nextLine();
            }

            ArrayList<String> playerNames = new ArrayList<>();

            for (int i = 1; i < 3; i++) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("╔═════════════---\n");
                sb2.append("║ Donnez un nom de joueur pour le joueur n°" + Integer.toString(i) + "\n");
                sb2.append("╠════════---  \n");
                sb2.append("║ Réponse : ");
                skipWin(sb2.toString(), false);

                String playerName = sc.nextLine();
                while (playerName.length() < 3 && playerNames.contains(playerName)){
                    System.out.print("║ \tRéponse non acceptée.\n║ \tIl faut plus de 3 caractères et un nom différent des autres.\n║ Réponse : ");
                    playerName = sc.nextLine();
                }

                playerNames.add(playerName);
            }

            sb = new StringBuilder();
            sb.append("╔═════════════---\n");
            sb.append("║ Rajouter un joueur ? (1 pour oui)\n");
            sb.append("╠════════---  \n");
            sb.append("║ Réponse : ");
            skipWin(sb.toString(), false);

            boolean addPlayer = sc.nextInt() == 1;
            while (addPlayer){

                StringBuilder sb2 = new StringBuilder();
                sb2.append("╔═════════════---\n");
                sb2.append("║ Donnez un nom de joueur pour le joueur n°" + Integer.toString(playerNames.size() + 1) + "\n");
                sb2.append("╠════════---  \n");
                sb2.append("║ Réponse : ");
                skipWin(sb2.toString(), false);

                String playerName = sc.nextLine();
                while (playerName.length() < 3 && playerNames.contains(playerName)){
                    System.out.print("║ \tRéponse non acceptée.\n║ \tIl faut plus de 3 caractères et un nom différent des autres.\n║ Réponse : ");
                    playerName = sc.nextLine();
                }

                playerNames.add(playerName);

                sb = new StringBuilder();
                sb.append("╔═════════════---\n");
                sb.append("║ Joueur \""+ playerName + "\" a été rajouté !\n");
                sb.append("║ Rajouter un joueur ? (1 pour oui)\n");
                sb.append("╠════════---  \n");
                sb.append("║ Réponse : ");
                skipWin(sb.toString(), false);
                addPlayer = sc.nextInt() == 1;
            }

            GameVS gameVs = new GameVS(options.scenario, options.difficulty, gameName, playerNames);
            gameVs.run();

        }
        else if (choice == 3){

            File repertoire = new File("save/");
            String liste[] = repertoire.list();
            if (liste == null || liste.length == 0){
                sb.append("╔═════════════---\n");
                sb.append("║ Il n'y a pas de sauvegardes disponibles.\n");
                sb.append("╠════════---  \n");
                sb.append("║ Appuyez sur entrée ...");
                skipWin(sb.toString(), false);
                sc.nextLine();
                System.exit(0);
            }

            String gameName = getSaveFromfolder();
            if (gameName.substring(gameName.length() - 7, gameName.length() - 5).equals("VS")){
                GameVS gameVS = GameVS.restoreGameVS(gameName);
                gameVS.run();
            }
            else {
                Game game = Game.restoreGame(gameName);
                game.run();
            }
        }

        sc.close();

    }

}
