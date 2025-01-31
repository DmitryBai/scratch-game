package home.assignment.exception;

public class InvalidCommandArgumentsException extends BaseGameException {
    public InvalidCommandArgumentsException(String message) {
        super(message);
    }

    public InvalidCommandArgumentsException(String message, Throwable cause) {
        super(message, cause);
    }
}
