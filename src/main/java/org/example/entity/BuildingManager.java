package org.example.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
public class BuildingManager extends IdGenerator{


    @Column(name = "name")
    private String name;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Company company;

    @OneToMany(mappedBy = "buildingManager")
    private List<Building> buildings;

    public BuildingManager(String name, Company company) {
        this.name = name;
        this.company = company;
    }

    public BuildingManager() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }
}
