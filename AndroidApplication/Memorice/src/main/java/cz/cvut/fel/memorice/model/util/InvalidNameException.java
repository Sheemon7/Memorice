package cz.cvut.fel.memorice.model.util;

/**
 * The instances of this exception indicate the attempt to create a new entity with invalid name.
 * The class was used in previous version of the application and is now deprecated
 */
@Deprecated
public class InvalidNameException extends WrongNameException {

    public InvalidNameException() {
        super("The set has invalid name");
    }
}
