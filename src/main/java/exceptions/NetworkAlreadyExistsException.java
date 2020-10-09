package exceptions;

import java.io.Serializable;

public class NetworkAlreadyExistsException extends Exception implements Serializable{

    private static final long serialVersionUID = 1L;

    // Standard exception constructors
    public NetworkAlreadyExistsException() { super(); }
    public NetworkAlreadyExistsException(String msg){
        super(msg);
    }
    public NetworkAlreadyExistsException(String msg, Exception e){
        super(msg, e);
    }
}
