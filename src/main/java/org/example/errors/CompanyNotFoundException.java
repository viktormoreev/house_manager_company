package org.example.errors;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException(Long companyId) {
        super("Company with id " + companyId + " is not found");
    }
}