package cz.cvut.fel.memorice.model.util;

/**
 * The instances of this exception indicate that entity has been somehow given the wrong name
 */
public class WrongNameException extends Exception {

    public WrongNameException(String detailMessage) {
        super(detailMessage);
    }
}
