package org.example.errors;

public class OwnerNotFoundException extends Exception {
    public OwnerNotFoundException(Long id) {
        super("Owner with id " + id + " is not found");
    }
}
