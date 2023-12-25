package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Apartment;
import org.example.entity.Living;

import org.example.errors.ApartmentNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LivingDao {

    public static void createLiving(Living living, Long apartmentId) throws ApartmentNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Apartment apartment = session.get(Apartment.class,apartmentId);
            if(apartment==null)throw new ApartmentNotFoundException(apartmentId);
            else {
                living.setApartment(apartment);
                session.save(living);
                transaction.commit();
            }
        }
    }

    public static Living getLivingById(long id){
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

    public static void updateLiving(Living living){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(living);
            transaction.commit();
        }

    }

    public static void deleteLiving(Living living){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(living);
            transaction.commit();
        }

    }

}
