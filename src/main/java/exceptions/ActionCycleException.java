package exceptions;

import java.io.Serializable;

public class ActionCycleException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public ActionCycleException() { super(); }
    public ActionCycleException(String msg){
        super(msg);
    }
    public ActionCycleException(String msg, Exception e){
        super(msg, e);
    }
}
