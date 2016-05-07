package cz.cvut.fel.memorice.model.util;

/**
 * The instances of this exception indicate the attempt to create entity with empty label
 */
public class EmptyLabelException extends Exception {

    public EmptyLabelException() {
        super("Entity's label has been set to empty!");
    }
}
