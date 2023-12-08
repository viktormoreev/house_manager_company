package org.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;


@Entity
public class Owner extends IdGenerator{

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "owner")
    private List<Apartment> apartments;


}
