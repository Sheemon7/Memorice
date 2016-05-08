package cz.cvut.fel.memorice.model.util;

/**
 * The instances of this exception indicate the attempt to insert duplicate values/terms into entity
 */
public class DefinitionAlreadyUsedException extends Exception {

    public DefinitionAlreadyUsedException() {
        super("Term is already used in this set!");
    }
}
