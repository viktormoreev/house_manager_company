package org.example.errors;

public class NoBuildingManagersInTheCompanyException extends Exception {

    public NoBuildingManagersInTheCompanyException(String message) {
        super(message);
    }

    public NoBuildingManagersInTheCompanyException(String message, Throwable cause) {
        super(message, cause);
    }
}
