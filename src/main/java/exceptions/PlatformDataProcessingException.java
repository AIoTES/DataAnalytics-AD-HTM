package exceptions;

import javax.ws.rs.core.Response;
import java.io.Serializable;

/**
 * This custom exception is used for communicating errors that occurred when trying to retrieve data from an IoT-Platform.
 * It is possible to provide custom messages as well as a custom error code for when the exception is sent to the frontend.
 */
public class PlatformDataProcessingException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * This is the default web response associated with this exception.
     */
    private Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

    /**
     * This is the getter for the {@link #response} property.
     * @return The {@link Response} set for this instance of {@link PlatformDataProcessingException}.
     */
    public Response getResponse() {return this.response;}

    /**
     * This is the getter for the error code of the web response of this class.
     * @return The {@link Response.Status} of the {@link #response} property of this class.
     */
    public Response.Status getResponseStatus() {return (this.response.getStatusInfo()).toEnum();}

    // Standard exception constructors
    public PlatformDataProcessingException(){super();}
    public PlatformDataProcessingException(String msg){super(msg);}
    public PlatformDataProcessingException(String msg, Exception e){super(msg, e);}

    /**
     * This constructor allows for passing an error code to be used in the exception.
     * @param msg The exception message.
     * @param responseStatus The error code to be used in the generated web response when the exception is
     *                       thrown back to the frontend.
     */
    public PlatformDataProcessingException(String msg, Response.Status responseStatus){
        super(msg);
        this.response = Response.status(responseStatus).entity(responseStatus.getReasonPhrase()).build();
    }
}
