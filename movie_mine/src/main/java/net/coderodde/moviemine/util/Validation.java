package net.coderodde.moviemine.util;

/**
 * This class contains methods for validation.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class Validation {

    public static void checkNotNull(final Object reference, 
                                    final String message) {
        if (reference == null) {
            throw new NullPointerException(message);
        }
    }
    
    public static void checkIntegerNotNegative(final int integer,
                                               final String message) {
        if (integer < 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
