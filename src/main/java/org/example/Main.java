package org.example;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.*;
import org.example.entity.*;
import org.example.errors.ApartmentNotFoundException;
import org.example.errors.BuildingNotFoundException;
import org.example.errors.OwnerNotFoundException;

import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SessionFactoryUtil.getSessionFactory().openSession();
/*
        Company company1 = new Company("Gosho-OOD");
        CompanyDao.createCompany(company1);
        BuildingManager buildingManager = new BuildingManager("Gosho");
        BuildingManagerDao.createBuildingManager(1L,buildingManager);
        Building building1 = new Building(3,"Izgrev");
        BuildingDao.createBuilding(building1,1L);
        Apartment apartment1 = new Apartment(6,75.6 ,2);
        ApartmentDao.createApartment(apartment1 , 1L );
        Living living = new Living("Georgi",true, LocalDate.of(2004,9,28));
        try {
            LivingDao.createLiving(living,1L);
        } catch (ApartmentNotFoundException e) {
            throw new RuntimeException(e);
        }
        */
        /*
        Owner owner=new Owner("Gosho");
        try {
            OwnerDao.createOwner(owner,1L);
        } catch (ApartmentNotFoundException e) {
            throw new RuntimeException(e);
        }
*/

    }
}