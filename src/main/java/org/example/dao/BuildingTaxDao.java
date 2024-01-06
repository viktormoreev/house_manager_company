package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.*;
import org.example.errors.BuildingTaxNotFoundException;
import org.example.errors.InvalidValueForAmountException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class BuildingTaxDao {

    public static void create (BuildingTaxes buildingTaxes, Long buildingId){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();

            Building building = BuildingDao.getById(buildingId);
            buildingTaxes.setBuilding(building);
            session.save(buildingTaxes);

            transaction.commit();
        }
    }

    public static BuildingTaxes getBuildingTaxesById(long id){
        BuildingTaxes buildingTaxes;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            buildingTaxes = session.get(BuildingTaxes.class , id);
            transaction.commit();
        }
        return buildingTaxes;
    }

    public static TaxesToPay getTaxesToPayById(long id){

        TaxesToPay taxesToPay;

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            taxesToPay = session.get(TaxesToPay.class , id);
        }

        return taxesToPay;
    }


    public static void update(BuildingTaxes buildingTaxes, Long id){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            BuildingTaxes newBuildingTaxes = session.get(BuildingTaxes.class,id);
            if(newBuildingTaxes==null) try {
                throw new BuildingTaxNotFoundException(id);
            } catch (BuildingTaxNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(buildingTaxes.getPer_living() != null && buildingTaxes.getPer_living() != BigDecimal.valueOf(0)){
                newBuildingTaxes.setPer_living(buildingTaxes.getPer_living());
            }
            if(buildingTaxes.getPer_pet() !=null && buildingTaxes.getPer_pet() != BigDecimal.valueOf(0)  ){
                newBuildingTaxes.setPer_pet(buildingTaxes.getPer_pet());
            }
            if(buildingTaxes.getPer_square_footage() != null && buildingTaxes.getPer_square_footage() != BigDecimal.valueOf(0)){
                newBuildingTaxes.setPer_square_footage(buildingTaxes.getPer_square_footage());
            }
            session.saveOrUpdate(newBuildingTaxes);
            transaction.commit();
        }

    }

    public static void delete(Long id){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            BuildingTaxes buildingTaxes = session.get(BuildingTaxes.class, id);
            session.delete(buildingTaxes);
            transaction.commit();
        }
    }

    public static void addTaxToPayForApartment (Long apartmentId){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Apartment apartment = ApartmentDao.getById(apartmentId);

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BuildingTaxes> cr = cb.createQuery(BuildingTaxes.class);
            Root<BuildingTaxes> root = cr.from(BuildingTaxes.class);
            cr.select(root).where(cb.equal(root.get("building"),apartment.getBuilding().getId()));
            Query<BuildingTaxes> query = session.createQuery(cr);
            BuildingTaxes buildingTaxes = query.getSingleResult();
            System.out.println(buildingTaxes);
            BigDecimal finalTax = apartment.getArea().multiply(buildingTaxes.getPer_square_footage());
            finalTax = finalTax.add(buildingTaxes.getPer_pet().multiply(BigDecimal.valueOf(apartment.getPet())));

            List<Living> livings = LivingDao.getLivingByApartmentId(apartmentId);

            finalTax = finalTax.add(livings.stream()
                    .filter(living -> Period.between(living.getDate_of_birth(), LocalDate.now()).getYears() > 7 && living.isUse_elevator())
                    .map(living -> buildingTaxes.getPer_living())
                    .reduce(BigDecimal.ZERO, BigDecimal::add));

            TaxesToPay taxesToPay = new TaxesToPay(LocalDate.now(), finalTax, BigDecimal.valueOf(0), apartment);
            session.save(taxesToPay);


            System.out.println(finalTax);
            transaction.commit();

        }
    }

    public static void addTaxToPayForBuilding(Long buildingId){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            Building building = BuildingDao.getById(buildingId);
            ApartmentDao.getApartmentsByBuildingId(buildingId).stream().forEach(apartment -> {
                addTaxToPayForApartment(apartment.getId());
            });
        }
    }


    public static void payTaxForApartment(Long taxesToPayId, BigDecimal amount){
        if(amount.compareTo(BigDecimal.valueOf(0))<=0) try {
            throw new InvalidValueForAmountException(amount);
        } catch (InvalidValueForAmountException e) {
            throw new RuntimeException(e);
        }
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            TaxesToPay taxesToPay = getTaxesToPayById(taxesToPayId);
            BigDecimal oldTax = taxesToPay.getToPay();
            BigDecimal oldPaid = taxesToPay.getPayed();
            if( oldTax.subtract(amount ).compareTo(BigDecimal.valueOf(0)) >0 ){
                taxesToPay.setToPay(oldTax.subtract(amount));
                taxesToPay.setPayed(oldPaid.add(amount));
            }
            else{
                taxesToPay.setToPay(BigDecimal.valueOf(0));
                taxesToPay.setPayed(oldPaid.add(oldTax));
            }
            session.saveOrUpdate(taxesToPay);

            transaction.commit();
        }

    }


}
