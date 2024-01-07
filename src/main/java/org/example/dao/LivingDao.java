package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Apartment;
import org.example.entity.Building;
import org.example.entity.BuildingManager;
import org.example.entity.Living;

import org.example.errors.ApartmentNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LivingDao {

    /**
     * Creates a new Living entity and associates it with an existing apartment.
     *
     * @param living The Living object to be created and persisted.
     * @param apartmentId The ID of the Apartment to which the Living entity will be associated.
     * @throws ApartmentNotFoundException If an apartment with the specified ID does not exist.
     */
    public static void create(Living living, Long apartmentId) throws ApartmentNotFoundException {

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Apartment apartment = ApartmentDao.getById(apartmentId);
            living.setApartment(apartment);
            session.save(living);
            transaction.commit();
        }

    }

    /**
     * Retrieves a Living entity by its ID.
     *
     * @param id The ID of the Living entity to retrieve.
     * @return The found Living object.
     */
    public static Living getById(long id){
        Living living;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            living = session.get(Living.class , id);
            transaction.commit();
        }
        return living;
    }

    /**
     * Retrieves all Living entities in the database.
     *
     * @return A list of all Living objects.
     */
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

    /**
     * Retrieves all Living entities associated with a specific apartment.
     *
     * @param apartmentId The ID of the Apartment.
     * @return A list of Living objects associated with the specified apartment.
     * @throws ApartmentNotFoundException If an apartment with the specified ID does not exist.
     */
    public static List<Living> getLivingByApartmentId(Long apartmentId) throws ApartmentNotFoundException {

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

    /**
     * Retrieves all Living entities associated with a specific building.
     *
     * @param buildingId The ID of the Building.
     * @return A list of Living objects associated with the specified building.
     */
    public static List<Living> getLivingByBuildingId(Long buildingId){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            List<Living> livingList=new ArrayList<>();
            ApartmentDao.getApartmentsByBuildingId(buildingId).stream().forEach(apartment ->
            {

                try {
                    getLivingByApartmentId(apartment.getId()).stream().forEach(living -> {
                        livingList.add(living);
                    });
                } catch (ApartmentNotFoundException e) {
                    throw new RuntimeException(e);
                }

            });
            return livingList;

        }

    }


    /**
     * Updates the details of an existing Living entity.
     *
     * @param living The Living object to update.
     */
    public static void update(Living living){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(living);
            transaction.commit();
        }

    }

    /**
     * Deletes a Living entity from the database.
     *
     * @param living The Living object to be deleted.
     */
    public static void delete(Living living){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(living);
            transaction.commit();
        }

    }

    /**
     * Retrieves all Living entities sorted by name and age.
     *
     * @return A sorted list of Living objects by name and age.
     */
    public static List<Living> livingsByNameAndAge (){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){


            List<Living> livingList = session.createQuery("Select l From org.example.entity.Living l ",Living.class).getResultList();

            Comparator<Living> byName = Comparator.comparing(Living::getName);
            Comparator<Living> byAge = Comparator.comparing(Living::getDate_of_birth).reversed();

            livingList.sort(byName.thenComparing(byAge));
            return livingList;
        }
    }

}
