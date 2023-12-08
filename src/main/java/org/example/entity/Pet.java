package org.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class Pet extends IdGenerator {

    @Column(name = "use_elevator")
    private boolean use_elevator;

    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;

}
