package cz.cvut.fel.memorice.model.util;

/**
 * The instances of this exception indicate the attempt to insert duplicate values/terms into entity
 */
public class TermAlreadyUsedException extends Exception {

    public TermAlreadyUsedException() {
        super("Term is already used in this set!");
    }
}
