package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Apartment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ApartmentDao {
    public static void createApartment(Apartment apartment){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(apartment);
            transaction.commit();
        }
    }

    public static Apartment getApartmentById(long id){
        Apartment apartment;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            apartment = session.get(Apartment.class , id);
            transaction.commit();
        }
        return apartment;
    }

    public static List<Apartment> getApartments(){
        List<Apartment> apartments;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            apartments = session.createQuery("Select c From org.example.entity.Apartment c ",Apartment.class)
                    .getResultList();
            transaction.commit();
        }
        return apartments;
    }

    public static void updateApartment(Apartment apartment){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(apartment);
            transaction.commit();
        }

    }

    public static void deleteApartment(Apartment apartment){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(apartment);
            transaction.commit();
        }

    }

}
