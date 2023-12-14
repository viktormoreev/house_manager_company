package org.example.errors;

public class BuildingManagerNotFoundException extends RuntimeException {
    public BuildingManagerNotFoundException(Long id) {
        super("Company with id " + id + " is not found");
    }
}
