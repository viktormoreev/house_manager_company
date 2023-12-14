package org.example.entity;


import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Building extends IdGenerator{

    @Column(name = "floors", nullable = false)
    private int floors;

    @Column(name = "address", nullable = false)
    private String address ;

    @ManyToOne(fetch = FetchType.LAZY)
    private BuildingManager buildingManager;

    @OneToMany(mappedBy = "building")
    private Set<Apartment> apartments;

    public Building(int floors, String address) {
        this.floors = floors;
        this.address = address;
    }

    public Building() {
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BuildingManager getBuildingManager() {
        return buildingManager;
    }

    public void setBuildingManager(BuildingManager buildingManager) {
        this.buildingManager = buildingManager;
    }

    public Set<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }
}
