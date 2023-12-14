package org.example.errors;

public class CompanyNotFoundException extends Exception {

    public CompanyNotFoundException(Long companyId) {
        super("Company with id " + companyId + " is not found");
    }
}