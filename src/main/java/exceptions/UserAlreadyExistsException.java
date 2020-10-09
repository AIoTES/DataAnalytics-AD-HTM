package exceptions;

        import java.io.Serializable;

/**
 * This exception is used to communicate that there was an error trying to create a User.
 */
public class UserAlreadyExistsException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    // Standard exception constructors
    public UserAlreadyExistsException() { super(); }
    public UserAlreadyExistsException(String msg){
        super(msg);
    }
    public UserAlreadyExistsException(String msg, Exception e){
        super(msg, e);
    }
}
