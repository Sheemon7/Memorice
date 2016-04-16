package cz.cvut.fel.memorice.gui;

import java.util.Scanner;

import cz.cvut.fel.memorice.gui.IStdIn;

/**
 * Created by sheemon on 19.3.16.
 */
public class StandardInputReader implements IStdIn {

    private static StandardInputReader singleton = new StandardInputReader();
    private Scanner scanner = new Scanner(System.in);


    private StandardInputReader() {

    }

    public static StandardInputReader getInstance() {
        return singleton;
    }

    public int probe() {
        return scanner.nextInt();
    }



    public String readLine() {
        return scanner.next();
    }
}
