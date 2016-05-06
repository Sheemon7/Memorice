package cz.cvut.fel.memorice.model.util;

/**
 * Created by sheemon on 19.4.16.
 */
public class EmptyNameException extends Exception {

    public EmptyNameException() {
        super("Entity's name has been set to empty!");
    }
}
