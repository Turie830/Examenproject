package alchemy.exceptions;

/**
 * A class for exceptions signaling an illegal name.
 *
 * @author Arthur
 * @author Mauro
 * @author Obe
 *
 * @version 1.0
 */
public class IllegalNameException extends RuntimeException {

    /**
     * Initialize this new illegal name exception with the given illegal name.
     *
     * @param name
     *        The illegal name that caused this exception.
     *
     * @post The message of this new exception mentions the given illegal name.
     *     | new.getMessage().equals("Illegal name: " + name)
     */
    public IllegalNameException(String name) {
        super("Illegal name: " + name);
    }
}