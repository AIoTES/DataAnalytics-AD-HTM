package exceptions;

import java.io.Serializable;

/**
 * This exception is used to communicate that there was an error trying to login a User.
 */
public class InvalidUserDataException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    // Standard exception constructors
    public InvalidUserDataException() { super(); }
    public InvalidUserDataException(String msg){
        super(msg);
    }
    public InvalidUserDataException(String msg, Exception e){
        super(msg, e);
    }
}
