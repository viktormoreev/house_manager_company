package org.example.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Company extends IdGenerator{

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "company")
    private Set<BuildingManager> buildingManagers;

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

    public Set<BuildingManager> getBuildingManagers() {
        return buildingManagers;
    }

    public void setBuildingManagers(Set<BuildingManager> buildingManagers) {
        this.buildingManagers = buildingManagers;
    }
}
