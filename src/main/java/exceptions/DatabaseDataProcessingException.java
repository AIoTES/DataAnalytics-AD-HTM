package exceptions;

import javax.ws.rs.core.Response;
import java.io.Serializable;

/**
 *
 */
public class DatabaseDataProcessingException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    private Response.Status ResponseStatus = Response.Status.INTERNAL_SERVER_ERROR;
    public DatabaseDataProcessingException(){
        super();
    }
    public DatabaseDataProcessingException(String msg){
        super(msg);
    }
    public DatabaseDataProcessingException(String msg, Exception e){
        super(msg, e);
    }
    public DatabaseDataProcessingException(String msg, Response.Status responseStatus){
        super(msg);
        this.ResponseStatus = responseStatus;
    }

    public Response.Status getResponseStatus(){
        return this.ResponseStatus;
    }
}
