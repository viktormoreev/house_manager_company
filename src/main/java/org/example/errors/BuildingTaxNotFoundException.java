package org.example.errors;

public class BuildingTaxNotFoundException extends Exception {
    public BuildingTaxNotFoundException(Long id) {
        super("BuildingTax with id " + id + " is not found");
    }
}
