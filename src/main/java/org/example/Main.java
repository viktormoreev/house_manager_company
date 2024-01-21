package org.example;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.*;
import org.example.entity.BuildingTaxes;
import org.example.entity.Living;
import org.example.errors.ApartmentNotFoundException;
import org.example.errors.BuildingNotFoundException;
import org.example.errors.OwnerNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;


public class Main {
    public static void main(String[] args) {
        SessionFactoryUtil.getSessionFactory().openSession();

    }

}