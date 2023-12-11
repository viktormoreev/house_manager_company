package org.example.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
public class Building extends IdGenerator{

    @Column(name = "floors", nullable = false)
    private int floors;

    @Column(name = "address", nullable = false)
    private String address ;

    @ManyToOne(fetch = FetchType.LAZY)
    private BuildingManager buildingManager;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Company company;

    @OneToMany(mappedBy = "building")
    private List<Apartment> apartments;

    public Building(int floors, String address, Company company) {
        this.company=company;
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
}
