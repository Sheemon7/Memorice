package cz.cvut.fel.memorice.gui;

/**
 * Interface defining basic stdin functionality
 * @deprecated
 */
@Deprecated
public interface IStdIn {

    /**
     * Asks for an integer and returns its value. Should be used after a query
     * @return integer inserted by user
     */
    int probe();

    /**
     * Reads one whole line from stdin
     * @return
     */
    String readLine();
}
