package org.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Apartment extends IdGenerator{

    @Column(name = "number")
    private int number;
    @Column(name = "area")
    private double area;

    @OneToMany
    private List<Person> living;

}
