package org.example.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
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

    public void addOwner(Owner owner) {
        // Add the owner to this apartment's set of owners
        this.owners.add(owner);

        // Ensure this apartment is added to the owner's set of apartments
        // Check to avoid infinite recursion
        if (!owner.getApartments().contains(this)) {
            owner.getApartments().add(this);
        }
    }

    public void setNumber(int number) {
        if(number != 0 ){
            this.number = number;
        }
    }

    public void setArea(BigDecimal area) {
        if(area.equals(null) || area.compareTo(BigDecimal.valueOf(0))<=0){
            this.area = area;
        }
    }

    public void setPet(int pet) {
        if(pet != 0 ){
            this.pet = pet;
        }
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "owner_apartment",
            joinColumns = @JoinColumn(name = "apartment_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id")
    )
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apartment apartment = (Apartment) o;
        return Objects.equals(getId(), apartment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
