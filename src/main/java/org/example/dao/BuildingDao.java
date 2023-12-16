package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.BuildingManagerDTO;
import org.example.entity.Building;
import org.example.entity.BuildingManager;
import org.example.entity.Company;
import org.example.errors.BuildingManagerNotFoundException;
import org.example.errors.CompanyNotFoundException;
import org.example.errors.NoBuildingManagersInTheCompanyException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.min;


public class BuildingDao {

    public static void addBuilding(Building building, Long companyId ) throws CompanyNotFoundException, NoBuildingManagersInTheCompanyException, BuildingManagerNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company = CompanyDao.findCompany(session,companyId);
            List<BuildingManager> buildingManagers = CompanyDao.getBuildingManagersByCompanyId(companyId);
            if (buildingManagers.isEmpty())throw new NoBuildingManagersInTheCompanyException("No building managers found for creating the building");
            BuildingManager managerWithLeastBuildings = buildingManagers.stream()
                    .min(Comparator.comparingInt(manager -> {
                        try {
                            return BuildingManagerDao.getBuildingsByBuildingManagerId(manager.getId()).size();
                        } catch (BuildingManagerNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }))
                    .orElse(null);

            building.setBuildingManager(managerWithLeastBuildings);
            session.save(building);
            session.update(managerWithLeastBuildings);
            transaction.commit();
        }
    }



    public static Building getBuildingById(long id){
        Building building;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            building = session.get(Building.class , id);
            transaction.commit();
        }
        return building;
    }

    public static List<Building> getBuildings(){
        List<Building> buildings;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            buildings = session.createQuery("Select c From org.example.entity.Building c ",Building.class)
                    .getResultList();
            transaction.commit();
        }
        return buildings;
    }

    public static void updateBuilding(Building building){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(building);
            transaction.commit();
        }

    }

    public static void deleteBuilding(Building building){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(building);
            transaction.commit();
        }

    }


}
