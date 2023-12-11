package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Building;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BuildingDao {

    public static void createBuilding(Building building){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(building);
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

    public static void deleteCompany(Building building){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(building);
            transaction.commit();
        }

    }


}
