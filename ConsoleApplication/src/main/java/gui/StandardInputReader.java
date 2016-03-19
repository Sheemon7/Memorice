package gui;

import java.util.Scanner;

/**
 * Created by sheemon on 19.3.16.
 */
public class StandardInputReader implements IStdIn {

    Scanner scanner = new Scanner(System.in);

    public int probe() {
        return scanner.nextInt();
    }

    public String readLine() {
        return scanner.nextLine();
    }
}
