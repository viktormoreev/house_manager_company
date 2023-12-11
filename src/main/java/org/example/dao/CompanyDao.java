package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Company;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CompanyDao {

    public static void createCompany(Company company){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(company);
            transaction.commit();
        }
    }

    public static Company getCompanyById(long id){
        Company company;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            company = session.get(Company.class , id);
            transaction.commit();
        }
        return company;
    }

    public static List<Company> getCompanies(){
        List<Company> companies;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            companies = session.createQuery("Select c From org.example.entity.Company c ",Company.class)
                    .getResultList();
            transaction.commit();
        }
        return companies;
    }

    public static void updateCompany(Company company){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(company);
            transaction.commit();
        }

    }

    public static void deleteCompany(Company company){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(company);
            transaction.commit();
        }

    }


}
