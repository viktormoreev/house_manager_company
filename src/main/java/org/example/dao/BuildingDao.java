package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Building;
import org.example.entity.BuildingManager;
import org.example.entity.Company;
import org.example.errors.BuildingManagerNotFoundException;
import org.example.errors.BuildingNotFoundException;
import org.example.errors.CompanyNotFoundException;
import org.example.errors.NoBuildingManagersInTheCompanyException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.util.List;


public class BuildingDao {

    public static Long findManagerWithLeastBuildings(Long companyId){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            // Native SQL query with parameter
            String sqlQuery = "SELECT MIN(bm.id) AS building_manager_id " +
                    "FROM building_manager bm " +
                    "LEFT JOIN building b ON bm.id = b.buildingManager_id " +
                    "WHERE bm.company_id = :companyId " +
                    "GROUP BY bm.id " +
                    "ORDER BY COUNT(b.buildingManager_id) ASC " +
                    "LIMIT 1";

            BigInteger buildingManagerId = (BigInteger) session.createSQLQuery(sqlQuery)
                    .setParameter("companyId", companyId)
                    .uniqueResult();

            return buildingManagerId.longValue();
        }
    }

    public static void create (Building building, Long companyId ){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();

            BuildingManager buildingManager = null;
            try {
                buildingManager = BuildingManagerDao.findBuildingManager(session , BuildingDao.findManagerWithLeastBuildings(companyId));
            } catch (BuildingManagerNotFoundException e) {
                throw new RuntimeException(e);
            }
            building.setBuildingManager(buildingManager);
            session.saveOrUpdate(building);
            transaction.commit();
        }

    }

    public static void changeBuilding(Session session, Building building, Long companyId)  {
        Transaction transaction = session.beginTransaction();
        BuildingManager buildingManager = null;
        try {
            buildingManager = BuildingManagerDao.findBuildingManager(session , BuildingDao.findManagerWithLeastBuildings(companyId));
        } catch (BuildingManagerNotFoundException e) {
            throw new RuntimeException(e);
        }
        building.setBuildingManager(buildingManager);
        session.saveOrUpdate(building);
        transaction.commit();
    }



    public static Building getById(long id){
        Building building;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            building = session.get(Building.class , id);
            if(building == null){
                try {
                    throw new BuildingNotFoundException(id);
                } catch (BuildingNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
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
