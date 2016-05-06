package cz.cvut.fel.memorice.model.util;

/**
 * Created by sheemon on 18.3.16.
 */
public class NameAlreadyUsedException extends WrongNameException {

    public NameAlreadyUsedException() {
        super("Name is already used in database");
    }
}
