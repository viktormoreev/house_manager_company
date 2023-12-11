package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Owner;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OwnerDao {

    public static void createOwner(Owner owner){
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


}
