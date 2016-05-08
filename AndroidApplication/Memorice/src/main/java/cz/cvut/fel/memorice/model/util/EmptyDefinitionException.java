package cz.cvut.fel.memorice.model.util;

/**
 * The instances of this exception indicate the attempt to create entry with empty term/value
 */
public class EmptyDefinitionException extends Exception {

    public EmptyDefinitionException() {
        super("The set contains empty term!");
    }
}
