package org.example.entity;


import javax.persistence.*;
import java.util.List;


@Entity
public class Owner extends IdGenerator{


    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Apartment> apartments;


}
