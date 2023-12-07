package org.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class Building extends IdGenerator{

    @Column(name = "floors")
    private int floors;

    @Column(name = "address")
    private String address ;

    @ManyToOne(fetch = FetchType.LAZY)
    private BuildingManager buildingManager;



}
