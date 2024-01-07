package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.*;
import org.example.errors.ApartmentNotFoundException;
import org.example.errors.BuildingNotFoundException;
import org.example.errors.CompanyNotFoundException;
import org.example.errors.OwnerNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ApartmentDao {
    public static void create(Apartment apartment, Long buildingId)  {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Building building = session.get(Building.class,buildingId);
            if(building==null) try {
                throw new BuildingNotFoundException(buildingId);
            } catch (BuildingNotFoundException e) {
                throw new RuntimeException(e);
            }
            else {
                apartment.setBuilding(building);
                session.save(apartment);
                transaction.commit();
            }
        }
    }

    public static Apartment getById(long id){
        Apartment apartment;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            apartment = session.get(Apartment.class , id);
            if(apartment==null)throw  new ApartmentNotFoundException(id);
            transaction.commit();
        } catch (ApartmentNotFoundException e) {
            throw new RuntimeException(e);
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

    public static List<Apartment> getApartmentsByBuildingId(Long buildingId){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Apartment> cr = cb.createQuery(Apartment.class);
            Root<Apartment> root = cr.from(Apartment.class);
            cr.select(root).where(cb.equal(root.get("building").get("id"),buildingId));
            TypedQuery<Apartment> typedQuery = session.createQuery(cr);
            List<Apartment> apartments = typedQuery.getResultList();
            transaction.commit();
            return apartments;
        }

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

    public static void addOwnerToApartment(Long ownerId, Long apartmentId) throws ApartmentNotFoundException, OwnerNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Apartment apartment = session.get(Apartment.class,apartmentId);
            Owner owner = session.get(Owner.class,ownerId);
            if(apartment==null){
                throw new ApartmentNotFoundException(apartmentId);
            };
            apartment.getOwners().add(owner);
            owner.getApartments().add(apartment);
            transaction.commit();
        }
    }

    public static void addLivingToApartment(Living living, Long apartmentId) throws ApartmentNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Apartment apartment = session.get(Apartment.class,apartmentId);
            if(apartment==null){
                throw new ApartmentNotFoundException(apartmentId);
            };
            apartment.getLiving().add(living);
            living.setApartment(apartment);
            transaction.commit();
        }
    }

    public static Apartment findApartmentByTaxId( Long taxesToPayId){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String jpql = "SELECT a " +
                    "FROM TaxesToPay tp " +
                    "JOIN tp.apartment a " +
                    "WHERE tp.id =:taxesToPayId";

            Apartment apartment = session.createQuery(jpql, Apartment.class)
                    .setParameter("taxesToPayId", taxesToPayId)
                    .getSingleResult();

            return apartment;
        }
    }

}
