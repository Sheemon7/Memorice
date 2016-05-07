package cz.cvut.fel.memorice.model.util;

/**
 * Created by sheemon on 16.4.16.
 */
public class ConcurrentBuildingException extends RuntimeException {
    public ConcurrentBuildingException() {
        super("Builder is currently building another entity!");
    }
}
