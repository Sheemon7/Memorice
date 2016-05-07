package cz.cvut.fel.memorice.model.util;

/**
 * The instances of this exception indicate the attempt to create entity with already used name
 */
public class NameAlreadyUsedException extends WrongNameException {

    public NameAlreadyUsedException() {
        super("Name is already used in database");
    }
}
