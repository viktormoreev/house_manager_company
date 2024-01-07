package org.example.write_file;

import org.example.entity.Apartment;
import org.example.entity.Building;
import org.example.entity.BuildingManager;
import org.example.entity.Company;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FiscalBill {

    private Company company;
    private BuildingManager buildingManager;
    private Building building;
    private Apartment apartment;
    private BigDecimal tax;
    private LocalDate localDate = LocalDate.now();

    public FiscalBill(Company company, BuildingManager buildingManager, Building building, Apartment apartment, BigDecimal tax) {
        this.company = company;
        this.buildingManager = buildingManager;
        this.building = building;
        this.apartment = apartment;
        this.tax = tax;
    }

    @Override
    public String toString() {
        return
                " Date: " + localDate + "\n"+
                " Company: " + company.getName() + "\n"+
                " Building Manager: " + buildingManager.getName()+ "\n"+
                " Building: " + building.getId() + "\n"+
                " Building address: " + building.getAddress() + "\n"+
                " Apartment: " + apartment.getId() + "\n"+
                " Tax: " + tax + "\n"+ "\n" ;
    }
}
