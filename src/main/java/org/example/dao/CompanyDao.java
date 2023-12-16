package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.BuildingManagerDTO;
import org.example.dto.CompanyDTO;
import org.example.entity.BuildingManager;
import org.example.entity.Company;
import org.example.errors.CompanyNotFoundException;
import org.example.errors.NoCompaniesException;
import org.example.mapper.EntityMapper;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyDao {


    public static void createCompany(Company company){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(company);
            transaction.commit();
        }
    }

    public static CompanyDTO getCompanyById(long id) throws CompanyNotFoundException {
        CompanyDTO companyDTO;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company= findCompany(session,id);
            companyDTO = EntityMapper.mapCompanyToCompanyDTO(company);
            transaction.commit();
        }
        return companyDTO;
    }

    public static List<CompanyDTO> getCompanies() throws NoCompaniesException {
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
            Company company=findCompany(session,companyId);
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


    public static void updateCompany(Company company, Long companyId) throws CompanyNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company old_company = findCompany(session,companyId);
            company.setId(companyId);
            session.saveOrUpdate(old_company);
            transaction.commit();
        }
    }

    public static void deleteCompany(Long companyId) throws CompanyNotFoundException {

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company = findCompany(session,companyId);
            session.delete(company);
            transaction.commit();
        }
    }

    public static Company findCompany (Session session, Long companyId) throws CompanyNotFoundException {
        Company company=session.get(Company.class,companyId);
        if(company==null)throw new CompanyNotFoundException(companyId);
        return company;
    }




}
