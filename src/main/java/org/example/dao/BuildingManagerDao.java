package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.BuildingManagerDTO;
import org.example.entity.Building;
import org.example.entity.BuildingManager;
import org.example.entity.Company;
import org.example.errors.BuildingManagerNotFoundException;
import org.example.errors.CompanyNotFoundException;
import org.example.mapper.EntityMapper;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class BuildingManagerDao {


    /**
     * Creates a new BuildingManager and associates it with a company.
     *
     * @param buildingManager The BuildingManager object to be created and persisted.
     * @param companyId The ID of the Company with which the building manager will be associated.
     * @throws CompanyNotFoundException If a company with the specified ID does not exist.
     */
    public static void create(BuildingManager buildingManager, Long companyId) throws CompanyNotFoundException {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company company = CompanyDao.findCompanyById(session, companyId);
            buildingManager.setCompany(company);
            session.save(buildingManager);
            transaction.commit();
        }
    }


    /**
     * Retrieves a BuildingManager by its ID and converts it to a DTO.
     *
     * @param id The ID of the BuildingManager to retrieve.
     * @return A DTO representation of the BuildingManager.
     * @throws BuildingManagerNotFoundException If a building with the specified ID does not exist.
     */
    public static BuildingManagerDTO getById(long id) throws BuildingManagerNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            BuildingManager buildingManager= null;

            buildingManager = findBuildingManager(session,id);

            BuildingManagerDTO buildingManagerDTO = EntityMapper.mapBuildingManagerToDTO(buildingManager);
            transaction.commit();
            return buildingManagerDTO;
        }
    }


    /**
     * Retrieves all building managers and converts them to DTOs.
     *
     * @return A list of DTOs representing all BuildingManagers.
     */
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


    /**
     * Updates the details of an existing building manager.
     *
     * @param buildingManager The updated BuildingManager object.
     * @param buildingManagerId The ID of the building manager to be updated.
     * @throws BuildingManagerNotFoundException If a building with the specified ID does not exist.
     */
    public static void update(BuildingManager buildingManager, Long buildingManagerId) throws BuildingManagerNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();

            BuildingManager oldBuildingManager = findBuildingManager(session, buildingManagerId);

            buildingManager.setId(buildingManagerId);
            session.saveOrUpdate(buildingManager);
            transaction.commit();
        }
    }

    /**
     * Removes a building manager from their position, reassigning their buildings to other managers.
     *
     * @param buildingManagerId The ID of the BuildingManager to be removed.
     * @throws BuildingManagerNotFoundException If no building manager is found with the given ID.
     */
    public static void fire(Long buildingManagerId) throws BuildingManagerNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            BuildingManager buildingManager = null;

            buildingManager = findBuildingManager(session,buildingManagerId);

            Company company = buildingManager.getCompany();
            buildingManager.setCompany(null);

            buildingManager.getBuildings().stream().toList().forEach(building -> {
                try {
                    BuildingDao.changeBuilding(session,building,company.getId());
                } catch (BuildingManagerNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }



    /**
     * Retrieves a list of buildings managed by a specific building manager.
     *
     * @param buildingManagerId The ID of the BuildingManager.
     * @return A list of Buildings managed by the specified building manager.
     */
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

    /**
     * Finds a building manager by their ID.
     *
     * @param session The current Hibernate session.
     * @param buildingManagerId The ID of the BuildingManager.
     * @return The found BuildingManager object.
     * @throws BuildingManagerNotFoundException If no building manager is found with the given ID.
     */
    public static BuildingManager findBuildingManager (Session session, Long buildingManagerId) throws BuildingManagerNotFoundException {
        BuildingManager buildingManager=session.get(BuildingManager.class,buildingManagerId);
        if(buildingManager==null)throw new BuildingManagerNotFoundException(buildingManagerId);
        return buildingManager;
    }

    /**
     * Deletes a building manager by their ID.
     *
     * @param buildingManagerId The ID of the BuildingManager to be deleted.
     * @throws BuildingManagerNotFoundException If no building manager is found with the given ID.
     */
    public static void delete(Long buildingManagerId) throws BuildingManagerNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            BuildingManager buildingManager=null;
            buildingManager = BuildingManagerDao.findBuildingManager(session,buildingManagerId);

            session.delete(buildingManager);
            transaction.commit();
        }
    }

    /**
     * Retrieves a list of building managers sorted by their name and the buildings they manage.
     *
     * @return A list of BuildingManagers sorted by name and the number of buildings they manage.
     */
    public static List<BuildingManager> buildingManagersByBuildingAndName() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            String hql = "SELECT bm FROM BuildingManager bm " +
                    "JOIN bm.buildings b " +
                    "ORDER BY bm.name, b.id";

            List<BuildingManager> buildingManagerList = session.createQuery(hql, BuildingManager.class).getResultList();

            return buildingManagerList;
        }
    }

    /**
     * Retrieves a list of building managers sorted by their name and the size of buildings they manage.
     *
     * @return A list of BuildingManagers sorted by name and building size.
     */
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

    /**
     * Calculates the total due amount for a specific building manager based on taxes to pay.
     *
     * @param buildingManagerId The ID of the BuildingManager.
     * @return The total due amount as a BigDecimal.
     */
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

    /**
     * Calculates the total income for a specific building manager based on taxes paid.
     *
     * @param buildingManagerId The ID of the BuildingManager.
     * @return The total income as a BigDecimal.
     */
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

    /**
     * Finds a building manager based on a tax ID.
     *
     * @param taxesToPayId The tax ID associated with the building manager.
     * @return The BuildingManager object associated with the given tax ID.
     */
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
