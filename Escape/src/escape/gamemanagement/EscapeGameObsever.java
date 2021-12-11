package escape.gamemanagement;

import escape.exception.EscapeException;
import escape.required.GameObserver;

public class EscapeGameObsever implements GameObserver
{
    private String message;
    private String error;

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public void notify(String message)
    {
        this.message = message;
        resetError();
    }

    public void notify(String message, Throwable cause)
    {
       this.message = message;
       error = cause.getMessage();
    }

    private void resetError()
    {
        this.error = null;
    }

}
