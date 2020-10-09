package exceptions;

import java.io.Serializable;

/**
 * This exception is thrown when an entry, specified by the caller, was not found in database.
 */
public class MissingDatabaseEntryException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public MissingDatabaseEntryException() { super(); }
    public MissingDatabaseEntryException(String msg){
        super(msg);
    }
    public MissingDatabaseEntryException(String msg, Exception e){
        super(msg, e);
    }
}
