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

        /*
        Company company = new Company("DHL");
        CompanyDao.create(company);
        BuildingManager buildingManager = new BuildingManager("Gosho");
        BuildingManagerDao.create(buildingManager,1l);
        Building building =new Building(10,"vitosha");
        BuildingDao.create(building,1l);
        Apartment apartment = new Apartment(10,BigDecimal.valueOf(10),4);
        ApartmentDao.create(apartment,1l);
        BuildingTaxes buildingTaxes = new BuildingTaxes(BigDecimal.valueOf(10),BigDecimal.valueOf(5),BigDecimal.valueOf(3));
        BuildingTaxDao.create(buildingTaxes,1l);
        BuildingTaxDao.addTaxToPayForApartment(1l);
        */
        //BuildingTaxDao.addTaxToPayForApartment(1l);
        System.out.println(CompanyDao.companyIncome(1L));


    }


}