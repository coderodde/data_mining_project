package net.coderodde.util;

import java.io.File;

/**
 * This class contains methods for validation.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class Validation {

    /**
     * Checks that the input reference is not <code>null</code>.
     * 
     * @param reference the reference to check.
     * @param message   the error message.
     * @throws NullPointerException if <code>reference</code> is 
     *                              <code>null</code>.
     */
    public static void checkNotNull(final Object reference, 
                                    final String message) {
        if (reference == null) {
            throw new NullPointerException(message);
        }
    }
    
    /**
     * Checks that the input integer is not negative.
     * 
     * @param integer the integer to check.
     * @param message the error message.
     * @throws IllegalArgumentException if <code>integer</code> is less than 
     *                                  zero.
     */
    public static void checkIntegerNotNegative(final int integer,
                                               final String message) {
        if (integer < 0) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * Checks that <code>file</code> exists.
     * 
     * @param file    the file to check.
     * @param message the error message.
     * @throws IllegalArgumentException if <code>file</code> does not exist.
     */
    public static void checkFileExists(final File file,
                                       final String message) {
        if (!file.exists()) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * Checks that <code>file</code> is a directory.
     * 
     * @param file    the file to check.
     * @param message the error message.
     * @throws IllegalArgumentException if <code>file</code> is not a directory.
     */
    public static void checkFileIsDirectory(final File file, 
                                            final String message) {
        if (!file.isDirectory()) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * Checks that <code>file</code> is a regular file.
     * 
     * @param file    the file to check.
     * @param message the error message.
     * @throws IllegalArgumentException if <code>file</code> is not a regular
     *                                  file.
     */
    public static void checkFileIsRegular(final File file, 
                                          final String message) {
        if (!file.isFile()) {
            throw new IllegalArgumentException(message);
        }
    }
}
