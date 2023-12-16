package org.example.errors;

public class NoCompaniesException extends Exception {
    public NoCompaniesException(){
        super("No companies left");
    }
}
