package home.assignment.exception;

public class BaseGameException extends RuntimeException {

    public BaseGameException(String message) {
        super(message);
    }

    public BaseGameException(String message, Throwable cause) {
        super(message, cause);
    }
}
