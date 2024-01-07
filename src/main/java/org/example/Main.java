package org.example;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.*;
import org.example.errors.ApartmentNotFoundException;
import org.example.errors.OwnerNotFoundException;


public class Main {
    public static void main(String[] args) {
        SessionFactoryUtil.getSessionFactory().openSession();

        try {
            ApartmentDao.addOwnerToApartment(3l,1l);
        } catch (ApartmentNotFoundException e) {
            throw new RuntimeException(e);
        } catch (OwnerNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


}