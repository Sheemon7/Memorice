package cz.cvut.fel.memorice.model.util;

/**
 * Created by sheemon on 2.5.16.
 */
public class EmptyTermException extends Exception {

    public EmptyTermException() {
        super("The set contains empty term!");
    }
}
