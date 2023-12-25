package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Apartment;
import org.example.entity.Owner;
import org.example.errors.ApartmentNotFoundException;
import org.example.errors.OwnerNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OwnerDao {

    public static void createOwner(Owner owner)  {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(owner);
            transaction.commit();
        }
    }

    public static Owner getOwnerById(long id){
        Owner owner;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            owner = session.get(Owner.class , id);
            transaction.commit();
        }
        return owner;
    }


    public static void updateOwner(Owner owner){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(owner);
            transaction.commit();
        }
    }

    public static void deleteOwner(Owner owner){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(owner);
            transaction.commit();
        }
    }

    public static void addApartmentToOwner(Long ownerId, Long apartmentId) throws ApartmentNotFoundException, OwnerNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Apartment apartment = session.get(Apartment.class,apartmentId);
            if(apartment==null){
                throw new ApartmentNotFoundException(apartmentId);
            };
            Owner owner=session.get(Owner.class,ownerId);
            if(owner==null){
                throw new OwnerNotFoundException(ownerId);
            }
            owner.getApartments().add(apartment);
            apartment.getOwners().add(owner);
            transaction.commit();
        }
    }

}
