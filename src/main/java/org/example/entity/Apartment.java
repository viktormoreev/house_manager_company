package org.example.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
public class Apartment extends IdGenerator{

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "area", nullable = false)
    private double area;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Building building;

    @OneToMany(mappedBy = "apartment")
    private List<Living> living;

    @OneToOne(mappedBy = "apartment")
    private Owner owner;

    @Column(name = "pet_using_common_areas")
    private boolean pet_using_common_areas;

    public Apartment(int number, double area, Building building, boolean pet_using_common_areas) {
        this.number = number;
        this.area = area;
        this.building = building;
        this.pet_using_common_areas = pet_using_common_areas;
    }

    public Apartment() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public List<Living> getLiving() {
        return living;
    }

    public void setLiving(List<Living> living) {
        this.living = living;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public boolean isPet_using_common_areas() {
        return pet_using_common_areas;
    }

    public void setPet_using_common_areas(boolean pet_using_common_areas) {
        this.pet_using_common_areas = pet_using_common_areas;
    }
}
