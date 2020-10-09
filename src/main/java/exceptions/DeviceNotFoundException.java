package exceptions;

import java.io.Serializable;

/**
 * This exception gets thrown if a device was not found in database.
 */
public class DeviceNotFoundException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public DeviceNotFoundException() { super(); }
    public DeviceNotFoundException(String msg){
        super(msg);
    }
    public DeviceNotFoundException(String msg, Exception e){
        super(msg, e);
    }
}
