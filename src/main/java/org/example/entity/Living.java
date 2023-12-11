package org.example.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Living extends IdGenerator {

    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "use_elevator", nullable = false)
    private boolean use_elevator;


    @Column(name = "date_of_birth", nullable = false)
    private Date date_of_birth;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Apartment apartment;
}
