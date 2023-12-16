package org.example;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.*;
import org.example.entity.Building;
import org.example.errors.BuildingManagerNotFoundException;
import org.example.errors.CompanyNotFoundException;
import org.example.errors.NoBuildingManagersInTheCompanyException;

public class Main {
    public static void main(String[] args) {
        SessionFactoryUtil.getSessionFactory().openSession();

        Building building=new Building(10,"Vitosha 51");
        try {
            BuildingDao.addBuilding(building,1L);
        } catch (CompanyNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoBuildingManagersInTheCompanyException | BuildingManagerNotFoundException e) {
            throw new RuntimeException(e);
        }


/*
       Building building=new Building(7,"Vitosha 14");
        try {
            BuildingDao.addBuilding(building,1L);
        } catch (CompanyNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoBuildingManagersInTheCompanyException e) {
            throw new RuntimeException(e);
        }
 */
    }


}