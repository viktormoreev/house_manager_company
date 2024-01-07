package org.example.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Owner extends IdGenerator{


    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "owners")
    private Set<Apartment> apartments = new HashSet<>();

    public void addApartment(Apartment apartment) {
        // Add the apartment to the owner's set of apartments
        this.apartments.add(apartment);

        // Ensure the owner is added to the apartment's set of owners
        // Check to avoid infinite recursion
        if (!apartment.getOwners().contains(this)) {
            apartment.getOwners().add(this);
        }
    }

    public Owner(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return Objects.equals(getId(), owner.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
