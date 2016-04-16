package cz.cvut.fel.memorice.gui;

import cz.cvut.fel.memorice.gui.IStdOut;

/**
 * Created by sheemon on 19.3.16.
 */
public class StandardOutputWriter implements IStdOut {

    private static StandardOutputWriter singleton = new StandardOutputWriter();

    private StandardOutputWriter() {

    }

    public static StandardOutputWriter getInstance() {
        return singleton;
    }

    public void write(String string) {
        System.out.print(string);
    }

    public void writeLn(String string) {
        System.out.println(string);
    }
}
