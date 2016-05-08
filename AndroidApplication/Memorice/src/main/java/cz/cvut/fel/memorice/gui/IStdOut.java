package cz.cvut.fel.memorice.gui;

/**
 * Interface defining basic stdout functionality
 * @deprecated
 */
@Deprecated
public interface IStdOut {

    /**
     * Writes one line to stdout
     * @param string Line to be written.
     */
    void write(String string);

    /**
     * Writes one line to stdout and inserts empty line
     * @param string Line to be written.
     */
    void writeLn(String string);
}
