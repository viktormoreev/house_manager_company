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
import java.util.Comparator;
import java.util.List;

public class BuildingManagerDao {

    public static void create(BuildingManager buildingManager, Long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company company = CompanyDao.findCompany(session, companyId);
            buildingManager.setCompany(company);
            session.save(buildingManager);
            transaction.commit();
        }
    }


    public static BuildingManagerDTO getById(long id) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            BuildingManager buildingManager= null;
            try {
                buildingManager = findBuildingManager(session,id);
            } catch (BuildingManagerNotFoundException e) {
                throw new RuntimeException(e);
            }
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


    public static void update(BuildingManager buildingManager, Long buildingManagerId)  {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            try {
                BuildingManager oldBuildingManager = findBuildingManager(session, buildingManagerId);
            } catch (BuildingManagerNotFoundException e) {
                throw new RuntimeException(e);
            }
            buildingManager.setId(buildingManagerId);
            session.saveOrUpdate(buildingManager);
            transaction.commit();
        }
    }

    public static void fire(Long buildingManagerId) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            BuildingManager buildingManager = null;
            try {
                buildingManager = findBuildingManager(session,buildingManagerId);
            } catch (BuildingManagerNotFoundException e) {
                throw new RuntimeException(e);
            }
            Company company = buildingManager.getCompany();
            buildingManager.setCompany(null);

            buildingManager.getBuildings().stream().toList().forEach(building -> {
                BuildingDao.changeBuilding(session,building,company.getId());
            });
        }
    }



    public static List<Building> getBuildingsByBuildingManagerId(Long buildingManagerId) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
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

    public static void delete(Long buildingManagerId)  {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            BuildingManager buildingManager=null;
            try {
                buildingManager = BuildingManagerDao.findBuildingManager(session,buildingManagerId);
            } catch (BuildingManagerNotFoundException e) {
                throw new RuntimeException(e);
            }
            session.delete(buildingManager);
            transaction.commit();
        }
    }

}
