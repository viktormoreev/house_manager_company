package org.example.errors;

public class ApartmentNotFoundException extends Exception {
    public ApartmentNotFoundException(Long id) {
        super("Apartment with id " + id + " is not found");
    }
}
