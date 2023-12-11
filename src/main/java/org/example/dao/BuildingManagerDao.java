package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.BuildingManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BuildingManagerDao {

    public static void createBuildingManager(BuildingManager buildingManager){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(buildingManager);
            transaction.commit();
        }
    }

    public static BuildingManager getBuildingManagerById(long id){
        BuildingManager buildingManager;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            buildingManager = session.get(BuildingManager.class , id);
            transaction.commit();
        }
        return buildingManager;
    }

    public static List<BuildingManager> getBuildingManagers(){
        List<BuildingManager> buildingManagers;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            buildingManagers = session.createQuery("Select c From org.example.entity.BuildingManager c ",BuildingManager.class)
                    .getResultList();
            transaction.commit();
        }
        return buildingManagers;
    }

    public static void updateBuildingManager(BuildingManager buildingManager){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(buildingManager);
            transaction.commit();
        }

    }

    public static void deleteCompany(BuildingManager buildingManager){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(buildingManager);
            transaction.commit();
        }

    }


}
