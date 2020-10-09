package exceptions;

import java.io.Serializable;

public class NetworkDoesNotExistException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    // Standard exception constructors
    public NetworkDoesNotExistException() { super(); }
    public NetworkDoesNotExistException(String msg){
        super(msg);
    }
    public NetworkDoesNotExistException(String msg, Exception e){
        super(msg, e);
    }
}
