package org.example.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "building_manager")
public class BuildingManager extends IdGenerator{

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @OneToMany(mappedBy = "buildingManager")
    private List<Building> buildings;

    public BuildingManager(String name){
        this.name=name;
    }

    @Override
    public String toString() {
        return "BuildingManager{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
