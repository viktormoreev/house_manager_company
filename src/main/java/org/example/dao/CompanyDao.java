package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.BuildingManagerDTO;
import org.example.dto.CompanyDTO;
import org.example.entity.*;
import org.example.errors.CompanyNotFoundException;
import org.example.errors.NoCompaniesException;
import org.example.mapper.EntityMapper;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyDao {


    /**
     * Creates a new Company record in the database.
     *
     * @param company The Company object to be created and persisted.
     */
    public static void create(Company company){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(company);
            transaction.commit();
        }
    }

    /**
     * Retrieves a Company by its ID and converts it to a DTO.
     *
     * @param id The ID of the Company to retrieve.
     * @return A DTO representation of the Company.
     * @throws CompanyNotFoundException If a company with the specified ID does not exist.
     */
    public static CompanyDTO getById(long id) throws CompanyNotFoundException {
        CompanyDTO companyDTO;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company= null;

            company = findCompanyById(session,id);

            companyDTO = EntityMapper.mapCompanyToCompanyDTO(company);
            transaction.commit();
        }
        return companyDTO;
    }

    /**
     * Retrieves all companies and converts them to DTOs.
     *
     * @return A list of DTOs representing all Companies.
     * @throws NoCompaniesException If no companies exist in the database.
     */
    public static List<CompanyDTO> getCompaniesDTO() throws NoCompaniesException {
        List<Company> companies;
        List<CompanyDTO> companiesDTO;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            companies = session.createQuery("Select c From org.example.entity.Company c ",Company.class)
                    .getResultList();
            if(companies.isEmpty()){
                throw new NoCompaniesException();
            }
            companiesDTO = companies.stream().map(company -> CompanyDTO.CompanyToDTO(company)).collect(Collectors.toList());
            transaction.commit();
        }
        return companiesDTO;
    }

    /**
     * Retrieves all building managers associated with a specific company.
     *
     * @param companyId The ID of the Company.
     * @return A list of BuildingManagers associated with the specified company.
     * @throws CompanyNotFoundException If a company with the specified ID does not exist.
     */
    public static List<BuildingManager> getBuildingManagersByCompanyId(Long companyId) throws CompanyNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company= findCompanyById(session,companyId);// did I need to have this ?
            CriteriaBuilder cb =session.getCriteriaBuilder();
            CriteriaQuery<BuildingManager> cr = cb.createQuery(BuildingManager.class);
            Root<BuildingManager> root = cr.from(BuildingManager.class);
            cr.select(root).where(cb.equal(root.get("company").get("id"),companyId));
            TypedQuery<BuildingManager> typedQuery = session.createQuery(cr);
            List<BuildingManager> buildingManagers = typedQuery.getResultList();
            transaction.commit();
            return buildingManagers;
        }
    }

    /**
     * Retrieves all building managers associated with a specific company and converts them to DTOs.
     *
     * @param companyId The ID of the Company.
     * @return A list of DTOs representing BuildingManagers associated with the specified company.
     * @throws CompanyNotFoundException If a company with the specified ID does not exist.
     */
    public static List<BuildingManagerDTO> getBuildingManagersDTOByCompanyId(Long companyId) throws CompanyNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company= findCompanyById(session,companyId);
            CriteriaBuilder cb =session.getCriteriaBuilder();
            CriteriaQuery<BuildingManager> cr = cb.createQuery(BuildingManager.class);
            Root<BuildingManager> root = cr.from(BuildingManager.class);
            cr.select(root).where(cb.equal(root.get("company").get("id"),companyId));
            TypedQuery<BuildingManager> typedQuery = session.createQuery(cr);
            List<BuildingManager> buildingManagers = typedQuery.getResultList();
            List<BuildingManagerDTO> buildingManagerDTOS=EntityMapper.mapBuildingManagerListToDTO(buildingManagers);
            transaction.commit();
            return buildingManagerDTOS;
        }
    }


    /**
     * Updates the details of an existing company.
     *
     * @param company The updated Company object.
     * @param companyId The ID of the Company to be updated.
     */
    public static void updateCompany(Company company, Long companyId) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(company);
            transaction.commit();
        }
    }

    /**
     * Deletes a Company by its ID.
     *
     * @param companyId The ID of the Company to be deleted.
     * @throws CompanyNotFoundException If a company with the specified ID does not exist.
     */
    public static void delete(Long companyId) throws CompanyNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company = null;

            company = findCompanyById(session,companyId);

            session.delete(company);
            transaction.commit();
        }
    }

    /**
     * Finds a company by its ID.
     *
     * @param session The current Hibernate session.
     * @param companyId The ID of the Company.
     * @return The found Company object.
     * @throws CompanyNotFoundException If a company with the specified ID does not exist.
     */
    public static Company findCompanyById(Session session, Long companyId) throws CompanyNotFoundException {
        Company company=session.get(Company.class,companyId);
        if(company==null) throw new CompanyNotFoundException(companyId);

        return company;
    }


    /**
     * Retrieves a list of companies sorted by their total income.
     *
     * @return A list of Companies sorted by total income.
     */
    public static List<Company> companiesByIncome() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            String hql = "SELECT c FROM Company c " +
                    "JOIN c.buildingManagers bm " +
                    "JOIN bm.buildings b " +
                    "JOIN b.apartments a " +
                    "JOIN a.taxesToPay tp " +
                    "GROUP BY c " +
                    "ORDER BY SUM(tp.payed) DESC";

            List<Company> companyList = session.createQuery(hql, Company.class).getResultList();

            return companyList;
        }
    }

    /**
     * Calculates the total income for a specific company based on taxes paid.
     *
     * @param companyId The ID of the Company.
     * @return The total income as a BigDecimal.
     */
    public static BigDecimal companyIncome(Long companyId) {
        BigDecimal totalIncome = BigDecimal.ZERO; // Default to zero
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String jpql = "SELECT SUM(tp.payed) AS TotalIncome " +
                    "FROM Company c " +
                    "JOIN c.buildingManagers bm " +
                    "JOIN bm.buildings b " +
                    "JOIN b.apartments a " +
                    "JOIN a.taxesToPay tp " +
                    "WHERE c.id =:companyId";

            totalIncome = session.createQuery(jpql, BigDecimal.class)
                    .setParameter("companyId", companyId) // Assuming the ID is of type Long
                    .getSingleResult();
        }

        return totalIncome;
    }

    /**
     * Calculates the total due amount for a specific company based on taxes to be paid.
     *
     * @param companyId The ID of the Company.
     * @return The total due amount as a BigDecimal.
     */
    public static BigDecimal companyDueAmount(Long companyId) {
        BigDecimal totalIncome = BigDecimal.ZERO; // Default to zero
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String jpql = "SELECT SUM(tp.toPay) AS TotalIncome " +
                    "FROM Company c " +
                    "JOIN c.buildingManagers bm " +
                    "JOIN bm.buildings b " +
                    "JOIN b.apartments a " +
                    "JOIN a.taxesToPay tp " +
                    "WHERE c.id =:companyId";

            totalIncome = session.createQuery(jpql, BigDecimal.class)
                    .setParameter("companyId", companyId) // Assuming the ID is of type Long
                    .getSingleResult();
        }

        return totalIncome;
    }



    /**
     * Finds a company based on a tax ID.
     *
     * @param taxesToPayId The tax ID associated with the company.
     * @return The Company object associated with the given tax ID.
     */
    public static Company findCompanyByTaxId( Long taxesToPayId){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String jpql = "SELECT c " +
                    "FROM TaxesToPay tp " +
                    "JOIN tp.apartment a " +
                    "JOIN a.building b " +
                    "JOIN b.buildingManager bm " +
                    "JOIN bm.company c " +
                    "WHERE tp.id =:taxesToPayId";
            Company company = session.createQuery(jpql, Company.class)
                    .setParameter("taxesToPayId", taxesToPayId)
                    .getSingleResult();
            return company;
        }
    }


}
