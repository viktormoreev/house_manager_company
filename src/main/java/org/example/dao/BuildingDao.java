package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Building;
import org.example.entity.BuildingManager;
import org.example.entity.Company;
import org.example.errors.CompanyNotFoundException;
import org.example.errors.NoBuildingManagersInTheCompanyException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;


public class BuildingDao {

    public static void createBuilding(Building building, Long companyId ) throws CompanyNotFoundException, NoBuildingManagersInTheCompanyException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company = session.get(Company.class,companyId);
            if(company==null)throw new CompanyNotFoundException(companyId);
            else{
                CriteriaBuilder cb =session.getCriteriaBuilder();
                CriteriaQuery<BuildingManager> cr = cb.createQuery(BuildingManager.class);
                Root<BuildingManager> root = cr.from(BuildingManager.class);

                Join<BuildingManager, Building> buildingJoin = root.join("buildings", JoinType.LEFT);
                cr.select(root).where(cb.equal(root.get("company").get("id"),companyId))
                        .groupBy(root)
                        .orderBy(cb.asc(cb.count(buildingJoin)));

                TypedQuery<BuildingManager> typedQuery = session.createQuery(cr);
                typedQuery.setMaxResults(1);

                List<BuildingManager> result = typedQuery.getResultList();

                if (!result.isEmpty()) {
                    BuildingManager managerWithLeastBuildings = result.get(0);

                    // Associate the Building with the identified BuildingManager
                    building.setBuildingManager(managerWithLeastBuildings);
                    managerWithLeastBuildings.getBuildings().add(building);

                    // Save the Building and update the BuildingManager
                    session.save(building);
                    session.update(managerWithLeastBuildings);
                }
                else{
                    throw new NoBuildingManagersInTheCompanyException("No building managers found for creating the building");
                }
                transaction.commit();
            }
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
