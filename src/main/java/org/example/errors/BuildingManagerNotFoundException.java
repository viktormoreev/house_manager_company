package org.example.errors;

public class BuildingManagerNotFoundException extends Exception {
    public BuildingManagerNotFoundException(Long id) {
        super("Building Manager with id " + id + " is not found");
    }
}
