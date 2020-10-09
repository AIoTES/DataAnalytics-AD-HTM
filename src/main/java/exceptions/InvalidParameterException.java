package exceptions;

import java.io.Serializable;

/**
 * This exception is thrown when an parameter is invalid while calling a method.
 */
public class InvalidParameterException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public InvalidParameterException() { super(); }
    public InvalidParameterException(String msg){
        super(msg);
    }
    public InvalidParameterException(String msg, Exception e){
        super(msg, e);
    }
}
