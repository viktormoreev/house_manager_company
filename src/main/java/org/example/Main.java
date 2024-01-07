package org.example;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.*;
import org.example.entity.*;

import org.example.errors.CompanyNotFoundException;
import org.example.errors.NoCompaniesException;

import java.math.BigDecimal;
import java.time.LocalDate;


import org.example.errors.*;

import java.time.LocalDate;


public class Main {
    public static void main(String[] args) {
        SessionFactoryUtil.getSessionFactory().openSession();

        BuildingTaxDao.payTaxForApartment(3l,BigDecimal.valueOf(10));

    }


}