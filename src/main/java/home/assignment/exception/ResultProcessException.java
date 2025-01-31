package home.assignment.exception;

public class ResultProcessException extends BaseGameException {
    public ResultProcessException(String message) {
        super(message);
    }

    public ResultProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
