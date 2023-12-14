package org.example.errors;

public class BuildingNotFoundException extends Exception {
    public BuildingNotFoundException(Long id) {
        super("Building with id " + id + " is not found");
    }
}
