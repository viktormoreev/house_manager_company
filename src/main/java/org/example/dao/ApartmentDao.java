package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Apartment;
import org.example.entity.Building;
import org.example.entity.Company;
import org.example.entity.Owner;
import org.example.errors.ApartmentNotFoundException;
import org.example.errors.BuildingNotFoundException;
import org.example.errors.CompanyNotFoundException;
import org.example.errors.OwnerNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ApartmentDao {
    public static void createApartment(Apartment apartment, Long buildingId) throws BuildingNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Building building = session.get(Building.class,buildingId);
            if(building==null)throw new BuildingNotFoundException(buildingId);
            else {
                apartment.setBuilding(building);
                building.getApartments().add(apartment);
                session.save(apartment);
                transaction.commit();
            }
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

    public static void addOwnerToApartment(Long apartmentId, Long ownerId) throws ApartmentNotFoundException, OwnerNotFoundException {
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
            transaction.commit();
        }
    }

}
