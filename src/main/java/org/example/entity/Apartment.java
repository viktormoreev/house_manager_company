package org.example.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Apartment extends IdGenerator{

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "area", nullable = false)
    private BigDecimal area;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Building building;

    @OneToMany(mappedBy = "apartment")
    private Set<Living> living;

    @ManyToMany(mappedBy = "apartments")
    private Set<Owner> owners = new HashSet<>();

    @Column(name = "pet")
    private int pet;

    @OneToMany(mappedBy = "apartment")
    private Set<TaxesToPay> taxesToPay;

    public Apartment(int number, BigDecimal area, int pet) {
        this.number = number;
        this.area = area;
        this.pet = pet;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "number=" + number +
                ", area=" + area +
                ", pet=" + pet +
                "} " + super.toString();
    }
}
