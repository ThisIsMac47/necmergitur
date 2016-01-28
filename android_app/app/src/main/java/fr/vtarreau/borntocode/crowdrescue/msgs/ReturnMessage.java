package fr.vtarreau.borntocode.crowdrescue.msgs;

/**
 * Created by Vincent on 16/01/2016.
 */
public class ReturnMessage {
    public boolean 	status;
    public String 	message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
