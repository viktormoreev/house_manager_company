package org.example.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company extends IdGenerator{

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "company")
    private Set<BuildingManager> buildingManagers;

    public Company (String name){
        this.name=name;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
