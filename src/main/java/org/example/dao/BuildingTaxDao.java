package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.*;
import org.example.errors.ApartmentNotFoundException;
import org.example.errors.BuildingNotFoundException;
import org.example.errors.BuildingTaxNotFoundException;
import org.example.errors.InvalidValueForAmountException;
import org.example.write_file.FiscalBill;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class BuildingTaxDao {

    /**
     * Creates a new BuildingTaxes record and associates it with a specific building.
     *
     * @param buildingTaxes The BuildingTaxes object to be created and persisted.
     * @param buildingId The ID of the building to which the taxes will be associated.
     * @throws BuildingNotFoundException If a building with the specified ID does not exist.
     */
    public static void create (BuildingTaxes buildingTaxes, Long buildingId) throws BuildingNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();

            Building building = BuildingDao.getById(buildingId);
            buildingTaxes.setBuilding(building);
            session.save(buildingTaxes);

            transaction.commit();
        }
    }

    /**
     * Retrieves building taxes by its ID.
     *
     * @param id The ID of the BuildingTaxes to retrieve.
     * @return The found BuildingTaxes object.
     */
    public static BuildingTaxes getBuildingTaxesById(long id){
        BuildingTaxes buildingTaxes;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            buildingTaxes = session.get(BuildingTaxes.class , id);
            transaction.commit();
        }
        return buildingTaxes;
    }

    /**
     * Retrieves taxes to pay by its ID.
     *
     * @param id The ID of the TaxesToPay to retrieve.
     * @return The found TaxesToPay object.
     */
    public static TaxesToPay getTaxesToPayById(long id){

        TaxesToPay taxesToPay;

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            taxesToPay = session.get(TaxesToPay.class , id);
        }

        return taxesToPay;
    }


    /**
     * Updates the details of an existing BuildingTaxes record.
     *
     * @param buildingTaxes The updated BuildingTaxes object.
     * @param id The ID of the BuildingTaxes record to be updated.
     */
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


    /**
     * Deletes a BuildingTaxes record by its ID.
     *
     * @param id The ID of the BuildingTaxes record to be deleted.
     */
    public static void delete(Long id){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            BuildingTaxes buildingTaxes = session.get(BuildingTaxes.class, id);
            session.delete(buildingTaxes);
            transaction.commit();
        }
    }

    /**
     * Adds tax obligations for a specific apartment.
     *
     * @param apartmentId The ID of the apartment for which taxes are to be calculated and added.
     * @throws ApartmentNotFoundException If an apartment with the specified ID does not exist.
     */
    public static void addTaxToPayForApartment (Long apartmentId) throws ApartmentNotFoundException {
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

            transaction.commit();

        }
    }

    /**
     * Adds tax obligations for all apartments in a specific building.
     *
     * @param buildingId The ID of the building for which taxes are to be calculated and added for all apartments.
     * @throws BuildingNotFoundException If a building with the specified ID does not exist.
     */
    public static void addTaxToPayForBuilding(Long buildingId) throws BuildingNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            Building building = BuildingDao.getById(buildingId);
            ApartmentDao.getApartmentsByBuildingId(buildingId).stream().forEach(apartment -> {
                try {
                    addTaxToPayForApartment(apartment.getId());
                } catch (ApartmentNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /**
     * Processes the payment of taxes for a specific apartment.
     *
     * @param taxesToPayId The ID of the TaxesToPay record to be updated.
     * @param amount The amount to be paid.
     */
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
            if( oldTax.subtract(amount).compareTo(BigDecimal.valueOf(0)) >0 );
            else{
                amount = oldTax;
            }
            taxesToPay.setToPay(oldTax.subtract(amount));
            taxesToPay.setPayed(oldPaid.add(amount));
            writeFiscalBill(taxesToPayId,amount);
            session.saveOrUpdate(taxesToPay);

            transaction.commit();
        }
    }

    /**
     * Writes a fiscal bill for a paid tax.
     *
     * @param taxesToPayId The ID of the TaxesToPay record for which the bill is written.
     * @param amount The amount paid.
     */
    public static void writeFiscalBill(Long taxesToPayId, BigDecimal amount){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String fileName = "paid.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                Company company = CompanyDao.findCompanyByTaxId(taxesToPayId);
                BuildingManager buildingManager = BuildingManagerDao.findBuildingManagerByTaxId(taxesToPayId);
                Building building = BuildingDao.findBuildingByTaxId(taxesToPayId);
                Apartment apartment = ApartmentDao.findApartmentByTaxId(taxesToPayId);
                FiscalBill fiscalBill = new FiscalBill(company, buildingManager, building, apartment, amount);
                writer.write(fiscalBill.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }




}
