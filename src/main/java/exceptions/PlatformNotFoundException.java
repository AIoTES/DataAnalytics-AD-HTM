package exceptions;

import java.io.Serializable;

/**
 * This exception is used to communicate that there was an error trying to resolve the id of an IoT-Platform.
 */
public class PlatformNotFoundException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    // Standard exception constructors
    public PlatformNotFoundException() { super(); }
    public PlatformNotFoundException(String msg){
        super(msg);
    }
    public PlatformNotFoundException(String msg, Exception e){
        super(msg, e);
    }
}
