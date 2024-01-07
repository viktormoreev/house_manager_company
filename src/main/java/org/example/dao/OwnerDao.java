package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Owner;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class OwnerDao {



    /**
     * Creates a new Owner and persists it to the database.
     *
     * @param owner The Owner object to be created and persisted.
     */
    public static void createOwner(Owner owner)  {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(owner);
            transaction.commit();
        }
    }

    /**
     * Retrieves an Owner entity by its ID.
     *
     * @param id The ID of the Owner to retrieve.
     * @return The found Owner object.
     */
    public static Owner getById(long id){
        Owner owner;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            owner = session.get(Owner.class , id);
            transaction.commit();
        }
        return owner;
    }


    /**
     * Updates the details of an existing Owner entity.
     *
     * @param owner The Owner object with updated information to be saved.
     */
    public static void update(Owner owner){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(owner);
            transaction.commit();
        }
    }

    /**
     * Deletes an Owner entity from the database by its ID.
     *
     * @param ownerId The ID of the Owner to be deleted.
     */
    public static void delete(Long ownerId){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Owner owner = session.get(Owner.class,ownerId);
            session.delete(owner);
            transaction.commit();
        }
    }



}
