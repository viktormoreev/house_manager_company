package org.example.errors;

public class ThereIsOnlyOneManagerException extends RuntimeException {
    public ThereIsOnlyOneManagerException(String message) {
        super(message);
    }

    public ThereIsOnlyOneManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
