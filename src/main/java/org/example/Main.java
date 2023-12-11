package org.example;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.ApartmentDao;
import org.example.dao.BuildingDao;
import org.example.dao.BuildingManagerDao;
import org.example.dao.CompanyDao;
import org.example.entity.Apartment;
import org.example.entity.Building;
import org.example.entity.BuildingManager;
import org.example.entity.Company;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SessionFactoryUtil.getSessionFactory().openSession();

        Company company1 = new Company("Boqna-OOD");
        company1.setId(1L);
        //BuildingManager buildingManager = new BuildingManager("Gosho", company1);
        //BuildingManagerDao.createBuildingManager(buildingManager);
        //CompanyDao.createCompany(company1);

        Building building1 = new Building(3,"Izgrev" , company1);
        building1.setId(2L);
        Apartment apartment1 = new Apartment(5,75.6,building1,false);
        ApartmentDao.createApartment(apartment1 );
        //BuildingDao.createBuilding(building1);
        //BuildingDao.getBuildings();

    }
}