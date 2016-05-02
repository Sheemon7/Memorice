package cz.cvut.fel.memorice.gui;

import cz.cvut.fel.memorice.gui.IStdOut;

/**
 * {@inheritDoc}
 */
public class StandardOutputWriter implements IStdOut {

    private static StandardOutputWriter singleton = new StandardOutputWriter();

    private StandardOutputWriter() {

    }

    /**
     * Returns an instance of {@link StandardOutputWriter}
     * @return an instance of {@link StandardOutputWriter}
     */
    public static StandardOutputWriter getInstance() {
        return singleton;
    }

    /**
     * {@inheritDoc}
     */
    public void write(String string) {
        System.out.print(string);
    }

    /**
     * {@inheritDoc}
     */
    public void writeLn(String string) {
        System.out.println(string);
    }
}
