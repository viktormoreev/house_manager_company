package org.example.entity;

import com.sun.istack.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Company extends IdGenerator{

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "company")
    private List<BuildingManager> buildingManagers;

    @OneToMany(mappedBy = "company")
    private List<Building> buildings;

    public Company() {
    }

    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BuildingManager> getBuildingManagers() {
        return buildingManagers;
    }

    public void setBuildingManagers(List<BuildingManager> buildingManagers) {
        this.buildingManagers = buildingManagers;
    }
}
