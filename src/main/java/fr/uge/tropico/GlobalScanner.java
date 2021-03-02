package fr.uge.tropico;

import java.io.Closeable;
import java.io.IOException;
import java.util.Scanner;

public class GlobalScanner implements Closeable {

    public static GlobalScanner SYSTEM_IN = new GlobalScanner();
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Creates a GlobalScanner object
     */
    private GlobalScanner(){}

    /**
     * Returns an integer entered by the users
     * @return an integer
     */
    public int nextInt(){
        int result = 0;
        boolean waiting = true;
        do {
            if (scanner.hasNextInt()){
                result = scanner.nextInt();
                waiting = false;
            } else {
                System.out.println("\t(Veuillez entrer un entier.)");
            }
            scanner.nextLine();
        } while (waiting);
        return result;
    }

    /**
     * Returns a String entered by the user
     * @return a string
     */
    public String nextLine(){
        return scanner.nextLine();
    }

    @Override
    public void close() throws IOException {
        scanner.close();
    }


}
