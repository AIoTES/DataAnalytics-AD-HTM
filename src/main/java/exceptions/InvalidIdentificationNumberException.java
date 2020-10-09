package exceptions;

import java.io.Serializable;

/**
 * This exception is thrown if an identification number was invalid.
 */
public class InvalidIdentificationNumberException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public InvalidIdentificationNumberException() { super(); }
    public InvalidIdentificationNumberException(String msg){
        super(msg);
    }
    public InvalidIdentificationNumberException(String msg, Exception e){
        super(msg, e);
    }
}
