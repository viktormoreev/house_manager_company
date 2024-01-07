package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.*;
import org.example.errors.ApartmentNotFoundException;
import org.example.errors.BuildingNotFoundException;
import org.example.errors.CompanyNotFoundException;
import org.example.errors.OwnerNotFoundException;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;

public class ApartmentDao {

    /**
     * Creates a new apartment and associates it with an existing building.
     * This method opens a new Hibernate session and transaction.
     * If the building with the provided ID does not exist, it throws a BuildingNotFoundException.
     *
     * @param apartment The Apartment object to be created and persisted.
     * @param buildingId The ID of the Building to which the apartment will be associated.
     * @throws BuildingNotFoundException If a building with the specified ID does not exist in the database.
     */
    public static void create(Apartment apartment, Long buildingId) throws BuildingNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Building building = session.get(Building.class,buildingId);
            if(building==null)throw new BuildingNotFoundException(buildingId);
            else {
                apartment.setBuilding(building);
                session.save(apartment);
                transaction.commit();
            }
        }
    }

    /**
     * Retrieves an apartment by its ID.
     *
     * @param id The ID of the apartment to retrieve.
     * @return The found Apartment object.
     * @throws ApartmentNotFoundException If no apartment is found with the given ID.
     */
    public static Apartment getById(long id) throws ApartmentNotFoundException {
        Apartment apartment;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            apartment = session.get(Apartment.class, id);
            if (apartment == null)throw new ApartmentNotFoundException(id);
            transaction.commit();

            return apartment;
        }
    }

    /**
     * Retrieves all apartments in the database.
     *
     * @return A list of all Apartment objects.
     */
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

    /**
     * Retrieves a list of apartments associated with a specific building ID.
     *
     * @param buildingId The ID of the building.
     * @return A list of Apartment objects associated with the specified building.
     */
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

    /**
     * Updates the details of an existing apartment.
     *
     * @param apartment The updated Apartment object.
     * @param apartmentId The ID of the apartment to be updated.
     * @throws ApartmentNotFoundException If no apartment is found with the given ID.
     */
    public static void updateApartment(Apartment apartment, Long apartmentId) throws ApartmentNotFoundException {

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Apartment oldApartment = getById(apartmentId);

            oldApartment.setArea(apartment.getArea());
            oldApartment.setNumber(apartment.getNumber());
            oldApartment.setPet(apartment.getPet());

            session.saveOrUpdate(oldApartment);
            transaction.commit();
        }

    }

    /**
     * Deletes an apartment by its ID.
     *
     * @param apartmentId The ID of the apartment to be deleted.
     * @throws ApartmentNotFoundException If no apartment is found with the given ID.
     */
    public static void deleteApartment(Long apartmentId) throws ApartmentNotFoundException {

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Apartment apartment = getById(apartmentId);
            session.delete(apartment);
            transaction.commit();
        }

    }


    /**
     * Adds an owner to an apartment and updates the bidirectional relationship.
     *
     * @param ownerId The ID of the owner to be added to the apartment.
     * @param apartmentId The ID of the apartment to which the owner is to be added.
     * @throws ApartmentNotFoundException If the apartment with the specified ID does not exist.
     * @throws OwnerNotFoundException If the owner with the specified ID does not exist.
     */
    public static void addOwnerToApartment(Long ownerId, Long apartmentId) throws ApartmentNotFoundException, OwnerNotFoundException {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Apartment apartment = session.get(Apartment.class, apartmentId);
            Owner owner = session.get(Owner.class, ownerId);

            if (apartment == null) {
                throw new ApartmentNotFoundException(apartmentId);
            }
            if (owner == null) {
                throw new OwnerNotFoundException(ownerId);
            }

            // Manage the bidirectional relationship
            apartment.addOwner(owner);

            // Persist the changes
            session.saveOrUpdate(apartment);
            session.saveOrUpdate(owner);

            transaction.commit();
        }
    }

    /**
     * Adds a living entity to an apartment.
     *
     * @param living The Living object to be added to the apartment.
     * @param apartmentId The ID of the apartment to which the living entity is to be added.
     * @throws ApartmentNotFoundException If the apartment with the specified ID does not exist.
     */
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

    /**
     * Finds an apartment based on a tax ID.
     *
     * @param taxesToPayId The tax ID associated with the apartment.
     * @return The Apartment object associated with the given tax ID.
     */
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
