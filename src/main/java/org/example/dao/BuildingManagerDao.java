package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.BuildingManagerDTO;
import org.example.entity.Building;
import org.example.entity.BuildingManager;
import org.example.entity.Company;
import org.example.errors.BuildingManagerNotFoundException;
import org.example.errors.CompanyNotFoundException;
import org.example.errors.NoBuildingManagersInTheCompanyException;
import org.example.mapper.EntityMapper;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class BuildingManagerDao {

    public static void addBuildingManager(BuildingManager buildingManager, Long companyId) throws CompanyNotFoundException {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company company = CompanyDao.findCompany(session, companyId);
            buildingManager.setCompany(company);
            session.save(buildingManager);
            transaction.commit();
        }
    }


    public static BuildingManagerDTO getBuildingManagerById(long id) throws BuildingManagerNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            BuildingManager buildingManager= findBuildingManager(session,id);
            BuildingManagerDTO buildingManagerDTO = EntityMapper.mapBuildingManagerToDTO(buildingManager);
            transaction.commit();
            return buildingManagerDTO;
        }
    }


    public static List<BuildingManagerDTO> getBuildingManagers(){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            List<BuildingManager> buildingManagers = session.createQuery("Select c From org.example.entity.BuildingManager c ",BuildingManager.class)
                    .getResultList();
            List<BuildingManagerDTO> buildingManagersDTO = EntityMapper.mapBuildingManagerListToDTO(buildingManagers);
            transaction.commit();
            return buildingManagersDTO;
        }
    }


    public static void updateBuildingManager(BuildingManager buildingManager, Long buildingManagerId) throws BuildingManagerNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            BuildingManager oldBuildingManager = findBuildingManager(session, buildingManagerId);
            buildingManager.setId(buildingManagerId);
            session.saveOrUpdate(buildingManager);
            transaction.commit();
        }
    }

    public static void deleteBuildingManager(Long buildingManagerId) throws BuildingManagerNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            BuildingManager buildingManager = findBuildingManager(session,buildingManagerId);
            List<Building> buildingsToTransfer = new ArrayList<>(buildingManager.getBuildings());
            session.delete(buildingManager);
            buildingsToTransfer.stream().forEach(building -> {
                try {
                    BuildingDao.addBuilding(building,buildingManager.getId());
                } catch (CompanyNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (NoBuildingManagersInTheCompanyException | BuildingManagerNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            transaction.commit();
        }
    }

    public static List<Building> getBuildingsByBuildingManagerId(Long buildingManagerId) throws BuildingManagerNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            BuildingManager buildingManager = findBuildingManager(session,buildingManagerId);
            CriteriaBuilder cb =session.getCriteriaBuilder();
            CriteriaQuery<Building> cr = cb.createQuery(Building.class);
            Root<Building> root = cr.from(Building.class);
            cr.select(root).where(cb.equal(root.get("buildingManager").get("id"),buildingManagerId));
            TypedQuery<Building> typedQuery = session.createQuery(cr);
            List <Building> buildings= typedQuery.getResultList();
            transaction.commit();
            return buildings;
        }
    }

    public static BuildingManager findBuildingManager (Session session, Long buildingManagerId) throws BuildingManagerNotFoundException {
        BuildingManager buildingManager=session.get(BuildingManager.class,buildingManagerId);
        if(buildingManager==null)throw new BuildingManagerNotFoundException(buildingManagerId);
        return buildingManager;
    }


}
