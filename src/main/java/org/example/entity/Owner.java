package org.example.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;


@Entity
public class Owner extends IdGenerator{


    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    private Apartment apartment;


}
