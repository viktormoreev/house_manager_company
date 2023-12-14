package org.example.entity;


import javax.persistence.*;
import java.util.List;
import java.util.Set;


@Entity
public class Owner extends IdGenerator{


    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Apartment> apartments;

    public Owner(String name) {
        this.name = name;
    }

    public Owner() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }
}
