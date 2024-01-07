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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;


public class BuildingDao {

    /**
     * Finds the ID of the building manager who manages the least number of buildings in a given company.
     *
     * @param companyId The ID of the company to search within.
     * @return The ID of the building manager with the least buildings.
     */
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

    /**
     * Creates a new building and associates it with a building manager who has the least number of buildings.
     *
     * @param building The Building object to be created and persisted.
     * @param companyId The ID of the company to which the building is to be associated.
     * @throws BuildingManagerNotFoundException If no building manager can be found.
     */
    public static void create (Building building, Long companyId ) throws BuildingManagerNotFoundException {

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();

            BuildingManager buildingManager = null;

            buildingManager = BuildingManagerDao.findBuildingManager(session , BuildingDao.findManagerWithLeastBuildings(companyId));

            building.setBuildingManager(buildingManager);
            session.saveOrUpdate(building);
            transaction.commit();
        }

    }

    /**
     * Changes the building manager of an existing building to the one who currently manages the least number of buildings.
     *
     * @param session The current Hibernate session.
     * @param building The building to update.
     * @param companyId The company ID to search for a building manager within.
     * @throws BuildingManagerNotFoundException If no building manager can be found.
     */
    public static void changeBuilding(Session session, Building building, Long companyId) throws BuildingManagerNotFoundException {
        Transaction transaction = session.beginTransaction();
        BuildingManager buildingManager = null;

        buildingManager = BuildingManagerDao.findBuildingManager(session , BuildingDao.findManagerWithLeastBuildings(companyId));

        building.setBuildingManager(buildingManager);
        session.saveOrUpdate(building);
        transaction.commit();
    }


    /**
     * Retrieves a building by its ID.
     *
     * @param id The ID of the building to retrieve.
     * @return The found Building object.
     * @throws BuildingNotFoundException If no building is found with the given ID.
     */
    public static Building getById(long id) throws BuildingNotFoundException {
        Building building;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            building = session.get(Building.class , id);
            if(building == null)throw new BuildingNotFoundException(id);
            transaction.commit();
        }
        return building;
    }

    /**
     * Retrieves all buildings in the database.
     *
     * @return A list of all Building objects.
     */
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

    /**
     * Updates the details of an existing building.
     *
     * @param building The Building object to update.
     */
    public static void updateBuilding(Building building){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(building);
            transaction.commit();
        }

    }

    /**
     * Deletes a building from the database.
     *
     * @param building The Building object to be deleted.
     */
    public static void deleteBuilding(Building building){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(building);
            transaction.commit();
        }

    }

    /**
     * Calculates the total due amount for a specific building based on taxes to pay.
     *
     * @param buildingId The ID of the building.
     * @return The total due amount as a BigDecimal.
     */
    public static BigDecimal buildingDueAmount(Long buildingId) {
        BigDecimal totalDueAmount = BigDecimal.ZERO; // Default to zero
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String jpql = "SELECT SUM(tp.toPay) AS TotalIncome " +
                    "FROM Building b " +
                    "JOIN b.apartments a " +
                    "JOIN a.taxesToPay tp " +
                    "WHERE b.id =:buildingId";

            totalDueAmount = session.createQuery(jpql, BigDecimal.class)
                    .setParameter("buildingId", buildingId) // Assuming the ID is of type Long
                    .getSingleResult();
        }

        return totalDueAmount;
    }

    /**
     * Calculates the total income for a specific building based on taxes paid.
     *
     * @param buildingId The ID of the building.
     * @return The total income as a BigDecimal.
     */
    public static BigDecimal buildingIncome(Long buildingId) {
        BigDecimal totalIncome = BigDecimal.ZERO; // Default to zero
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String jpql = "SELECT SUM(tp.payed) AS TotalIncome " +
                    "FROM Building b " +
                    "JOIN b.apartments a " +
                    "JOIN a.taxesToPay tp " +
                    "WHERE b.id =:buildingId";

            totalIncome = session.createQuery(jpql, BigDecimal.class)
                    .setParameter("buildingId", buildingId) // Assuming the ID is of type Long
                    .getSingleResult();
        }

        return totalIncome;
    }

    /**
     * Finds a building based on a tax ID.
     *
     * @param taxesToPayId The tax ID associated with the building.
     * @return The Building object associated with the given tax ID.
     */
    public static Building findBuildingByTaxId( Long taxesToPayId){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String jpql = "SELECT b " +
                    "FROM TaxesToPay tp " +
                    "JOIN tp.apartment a " +
                    "JOIN a.building b " +
                    "WHERE tp.id =:taxesToPayId";
            Building building = session.createQuery(jpql, Building.class)
                    .setParameter("taxesToPayId", taxesToPayId)
                    .getSingleResult();
            return building;
        }
    }


}
