package fr.vtarreau.borntocode.crowdrescue.msgs;

/**
 * Created by Vincent on 16/01/2016.
 */
public class ErrorMessage {
    public String error;
    public String errorMessage;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
