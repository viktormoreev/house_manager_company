package org.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Company extends IdGenerator{
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "company")
    private List<BuildingManager> buildingManagers;


}
