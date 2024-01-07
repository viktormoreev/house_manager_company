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
import java.math.BigDecimal;
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

    public static List<BuildingManager> buildingManagersByBuildingAndName() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            String hql = "SELECT bm FROM BuildingManager bm " +
                    "JOIN bm.buildings b " +
                    "ORDER BY bm.name, b.id";

            List<BuildingManager> buildingManagerList = session.createQuery(hql, BuildingManager.class).getResultList();

            return buildingManagerList;
        }
    }

    public static List<BuildingManager> buildingManagersSortedByName() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            String hql = "SELECT DISTINCT bm FROM BuildingManager bm " +
                    "LEFT JOIN FETCH bm.buildings"; // Ensure we fetch buildings to avoid lazy loading issues.

            List<BuildingManager> buildingManagerList = session.createQuery(hql, BuildingManager.class).getResultList();

            Comparator<BuildingManager> byName = Comparator.comparing(BuildingManager::getName);

            Comparator<BuildingManager> byBuildingSize = Comparator
                    .comparing((BuildingManager bm) -> bm.getBuildings().size())
                    .reversed();

            buildingManagerList.sort(byName.thenComparing(byBuildingSize));

            return buildingManagerList;
        }
    }

    public static BigDecimal buildingManagerDueAmount(Long buildingManagerId) {
        BigDecimal totalDueAmount = BigDecimal.ZERO; // Default to zero
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String jpql = "SELECT SUM(tp.toPay) AS TotalIncome " +
                    "FROM BuildingManager bm " +
                    "JOIN bm.buildings b " +
                    "JOIN b.apartments a " +
                    "JOIN a.taxesToPay tp " +
                    "WHERE bm.id =:buildingManagerId";

            totalDueAmount = session.createQuery(jpql, BigDecimal.class)
                    .setParameter("buildingManagerId", buildingManagerId) // Assuming the ID is of type Long
                    .getSingleResult();
        }

        return totalDueAmount;
    }

    public static BigDecimal buildingManagerIncome(Long buildingManagerId) {
        BigDecimal totalIncome = BigDecimal.ZERO; // Default to zero
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String jpql = "SELECT SUM(tp.payed) AS TotalIncome " +
                    "FROM BuildingManager bm " +
                    "JOIN bm.buildings b " +
                    "JOIN b.apartments a " +
                    "JOIN a.taxesToPay tp " +
                    "WHERE bm.id =:buildingManagerId";

            totalIncome = session.createQuery(jpql, BigDecimal.class)
                    .setParameter("buildingManagerId", buildingManagerId) // Assuming the ID is of type Long
                    .getSingleResult();
        }

        return totalIncome;
    }

    public static BuildingManager findBuildingManagerByTaxId( Long taxesToPayId){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String jpql = "SELECT bm " +
                    "FROM TaxesToPay tp " +
                    "JOIN tp.apartment a " +
                    "JOIN a.building b " +
                    "JOIN b.buildingManager bm " +
                    "WHERE tp.id =:taxesToPayId";
            BuildingManager buildingManager  = session.createQuery(jpql, BuildingManager.class)
                    .setParameter("taxesToPayId", taxesToPayId)
                    .getSingleResult();
            return buildingManager;
        }
    }

}
