package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Building;
import org.example.entity.BuildingManager;
import org.example.entity.Company;
import org.example.errors.BuildingManagerNotFoundException;
import org.example.errors.CompanyNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class BuildingManagerDao {

    public static void createBuildingManager(Long companyId, BuildingManager buildingManager) throws CompanyNotFoundException {

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company = session.get(Company.class,companyId);
            if(company==null)throw new CompanyNotFoundException(companyId);
            else {
                buildingManager.setCompany(company);
                company.getBuildingManagers().add(buildingManager);
                session.save(buildingManager);
                transaction.commit();
            }
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

    public static List<BuildingManager> getBuildingManagersByCompanyId(Long companyId) throws CompanyNotFoundException {
        List<BuildingManager> buildingManagers;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company = session.get(Company.class,companyId);
            if(company==null)throw new CompanyNotFoundException(companyId);
            else{
                CriteriaBuilder cb =session.getCriteriaBuilder();
                CriteriaQuery<BuildingManager> cr = cb.createQuery(BuildingManager.class);
                Root<BuildingManager> root = cr.from(BuildingManager.class);

                cr.select(root).where(cb.equal(root.get("company").get("id"),companyId));
                TypedQuery<BuildingManager> typedQuery = session.createQuery(cr);
                buildingManagers = typedQuery.getResultList();
            }

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

    public static void deleteBuildingManager(Long buildingManagerId) throws BuildingManagerNotFoundException {

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            BuildingManager buildingManager = session.get( BuildingManager.class, buildingManagerId);
            if(buildingManager==null){
                throw new BuildingManagerNotFoundException(buildingManagerId);
            }
            CriteriaBuilder cb =session.getCriteriaBuilder();
            CriteriaQuery<BuildingManager> cr = cb.createQuery(BuildingManager.class);
            Root<BuildingManager> root = cr.from(BuildingManager.class);

            cr.select(root).where(cb.equal(root.get("company").get("id"),buildingManager.getCompany().getId())).where(cb.notEqual(root.get("id"),buildingManager.getId()));
            TypedQuery<BuildingManager> typedQuery = session.createQuery(cr);
            List<BuildingManager> buildingManagers = typedQuery.getResultList();

            List<Building> buildingsToTransfer = new ArrayList<>(buildingManager.getBuildings());
            int numOfManagers = buildingManagers.size();
            int index = 0;
            for (Building building : buildingsToTransfer) {
                BuildingManager targetManager = buildingManagers.get(index);
                building.setBuildingManager(targetManager);
                targetManager.getBuildings().add(building);

                // Move to the next manager in a round-robin fashion
                index = (index + 1) % numOfManagers;
            }
            session.delete(buildingManager);
            transaction.commit();
        }

    }


}
