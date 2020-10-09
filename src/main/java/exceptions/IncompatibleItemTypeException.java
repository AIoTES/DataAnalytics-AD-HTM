package exceptions;

import java.io.Serializable;

public class IncompatibleItemTypeException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public IncompatibleItemTypeException() { super(); }
    public IncompatibleItemTypeException(String msg){
        super(msg);
    }
    public IncompatibleItemTypeException(String msg, Exception e){
        super(msg, e);
    }
}
