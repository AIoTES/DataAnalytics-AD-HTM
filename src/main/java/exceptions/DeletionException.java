package exceptions;

import java.io.Serializable;

/**
 * Gets thrown if an error occurred while deletion process.
 */

public class DeletionException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public DeletionException() { super(); }
    public DeletionException(String msg){
        super(msg);
    }
    public DeletionException(String msg, Exception e){
        super(msg, e);
    }
}
