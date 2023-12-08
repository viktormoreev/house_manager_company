package org.example.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Apartment extends IdGenerator{

    @Column(name = "number")
    private int number;
    @Column(name = "area")
    private double area;

    @OneToMany(mappedBy = "apartment")
    private List<Living> living;

    @ManyToOne(fetch = FetchType.LAZY)
    private Owner owner;

    @OneToMany(mappedBy = "apartment")
    private List<Pet> pets;

}
