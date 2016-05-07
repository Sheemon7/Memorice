package cz.cvut.fel.memorice.model.util;

/**
 * The instances of this exception indicate attempt to launch another building process on hte builder
 * which is currently building another instance
 */
public class ConcurrentBuildingException extends RuntimeException {

    public ConcurrentBuildingException() {
        super("Builder is currently building another entity!");
    }
}
