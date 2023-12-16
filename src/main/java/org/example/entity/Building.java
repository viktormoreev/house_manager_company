package org.example.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToOne(mappedBy = "building")
    private BuildingTaxes buildingTaxes;

    public Building(int floors, String address) {
        this.floors = floors;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Building{" +
                "floors=" + floors +
                ", address='" + address + '\'' +
                "} " + super.toString();
    }
}
