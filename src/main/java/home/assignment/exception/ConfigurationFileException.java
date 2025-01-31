package home.assignment.exception;

public class ConfigurationFileException extends BaseGameException {

    public ConfigurationFileException(String message) {
        super(message);
    }

    public ConfigurationFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
