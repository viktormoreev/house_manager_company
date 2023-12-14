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
        BuildingManagerDao.deleteBuildingManager(3L);
        //BuildingManagerDao.getBuildingManagersByCompanyId(1L);
        //Company company1 = new Company("Gosho-OOD");
        //BuildingManager buildingManager = new BuildingManager("Mitko");
        //BuildingManagerDao.createBuildingManager(1L,buildingManager);
        //CompanyDao.createCompany(company1);
/*
        Company company1 = new Company("Boqna-OOD");
        // BuildingManager buildingManager = new BuildingManager("Gosho");
        //BuildingManagerDao.createBuildingManager(buildingManager);
        //CompanyDao.createCompany(company1);

        Building building1 = new Building(3,"Izgrev");
        building1.setId(2L);
        Apartment apartment1 = new Apartment(5,75.6,building1,2);
        ApartmentDao.createApartment(apartment1 );
        //BuildingDao.createBuilding(building1);
        //BuildingDao.getBuildings();
*/
    }
}