package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Apartment;
import org.example.entity.Building;
import org.example.entity.Living;

import org.example.errors.ApartmentNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class LivingDao {

    public static void create(Living living, Long apartmentId){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Apartment apartment = ApartmentDao.getById(apartmentId);
            living.setApartment(apartment);
            session.save(living);
            transaction.commit();
        }

    }

    public static Living getById(long id){
        Living living;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            living = session.get(Living.class , id);
            transaction.commit();
        }
        return living;
    }

    public static List<Living> getLivings(){
        List<Living> livings;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            livings = session.createQuery("Select c From org.example.entity.Living c ",Living.class)
                    .getResultList();
            transaction.commit();
        }
        return livings;
    }

    public static List<Living> getLivingByApartmentId(Long apartmentId){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Living> cr = cb.createQuery(Living.class);
            Root<Living> root = cr.from(Living.class);
            Apartment apartment = ApartmentDao.getById(apartmentId);
            cr.select(root).where(cb.equal(root.get("apartment"),apartment));
            Query<Living> query = session.createQuery(cr);
            List<Living> livings = query.getResultList();
            return livings;
        }

    }


    public static void update(Living living){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(living);
            transaction.commit();
        }

    }

    public static void delete(Living living){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(living);
            transaction.commit();
        }

    }

}
