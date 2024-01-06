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


    public static void create(Company company){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(company);
            transaction.commit();
        }
    }

    public static CompanyDTO getById(long id) {
        CompanyDTO companyDTO;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company= null;

            company = findCompany(session,id);

            companyDTO = EntityMapper.mapCompanyToCompanyDTO(company);
            transaction.commit();
        }
        return companyDTO;
    }

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

    public static List<BuildingManager> getBuildingManagersByCompanyId(Long companyId) throws CompanyNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company=findCompany(session,companyId);// did I need to have this ?
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

    public static List<BuildingManagerDTO> getBuildingManagersDTOByCompanyId(Long companyId) throws CompanyNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company=findCompany(session,companyId);
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


    public static void updateCompany(Company company, Long companyId) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(company);
            transaction.commit();
        }
    }

    public static void delete(Long companyId)  {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company = null;

            company = findCompany(session,companyId);

            session.delete(company);
            transaction.commit();
        }
    }

    public static Company findCompany (Session session, Long companyId)  {
        Company company=session.get(Company.class,companyId);
        if(company==null) try {
            throw new CompanyNotFoundException(companyId);
        } catch (CompanyNotFoundException e) {
            throw new RuntimeException(e);
        }
        return company;
    }

    public static List<Company> companiesByInitialCapital(){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            /*
            String hql = "SELECT c FROM Company c " +
                    "JOIN c.buildingManagers bm " +
                    "JOIN bm.buildings b " +
                    "JOIN b.apartments a " +
                    "JOIN a.taxesToPay tp " +
                    "GROUP BY c " +
                    "ORDER BY SUM(tp.payed) DESC";


            List<Company> companies = session.createQuery(hql, Company.class).getResultList();

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


             */
            List<Company> companies = session.createQuery("FROM Company", Company.class).getResultList();
            System.out.println(companies.isEmpty());
            transaction.commit();
            return companies;
        }
    }

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
                    .setParameter("companyId", 1L) // Assuming the ID is of type Long
                    .getSingleResult();

    }
        return totalIncome;
    }




}
