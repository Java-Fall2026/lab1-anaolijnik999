package ua.logistics.exception;

/**
 * Checked exception for invalid data encountered during file parsing.
 */
public class InvalidDataException extends Exception {

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}