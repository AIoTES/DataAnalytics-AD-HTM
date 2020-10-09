package exceptions;

import java.io.Serializable;

/**
 * This exception is thrown if a parameter is missing while a method call.
 */
public class MissingParameterException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public MissingParameterException() { super(); }
    public MissingParameterException(String msg){
        super(msg);
    }
    public MissingParameterException(String msg, Exception e){
        super(msg, e);
    }
}
