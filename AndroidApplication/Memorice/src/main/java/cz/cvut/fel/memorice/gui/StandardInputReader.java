package cz.cvut.fel.memorice.gui;

import java.util.Scanner;

import cz.cvut.fel.memorice.gui.IStdIn;

/**
 * {@inheritDoc}
 * @deprecated
 */
@Deprecated
public class StandardInputReader implements IStdIn {

    private static StandardInputReader singleton = new StandardInputReader();
    private Scanner scanner = new Scanner(System.in);


    private StandardInputReader() {

    }

    /**
     * Returns an instance of {@link StandardInputReader}
     * @return instance of {@link StandardInputReader}
     */
    public static StandardInputReader getInstance() {
        return singleton;
    }

    /**
     * {@inheritDoc}
     */
    public int probe() {
        return scanner.nextInt();
    }


    /**
     * {@inheritDoc}
     */
    public String readLine() {
        return scanner.next();
    }
}
