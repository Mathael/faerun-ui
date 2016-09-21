package com.exceptions;

/**
 * @author Leboc Philippe.
 */
public class CriticalHitException extends Exception {

    /**
     * @param message message qui sera affiché en console en attendant la gestion en IU
     */
    public CriticalHitException(String message) {
        super(message);
    }

}
