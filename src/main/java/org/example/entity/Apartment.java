package org.example.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Apartment extends IdGenerator{

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "area", nullable = false)
    private double area;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Building building;

    @OneToMany(mappedBy = "apartment")
    private Set<Living> living;

    @ManyToMany(mappedBy = "apartments")
    private Set<Owner> owners;

    @Column(name = "pet_using_common_areas")
    private int pet_using_common_areas;

    public Apartment(int number, double area, int pet_using_common_areas) {
        this.number = number;
        this.area = area;
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

    public Set<Living> getLiving() {
        return living;
    }

    public void setLiving(Set<Living> living) {
        this.living = living;
    }

    public Set<Owner> getOwners() {
        return owners;
    }

    public void setOwners(Set<Owner> owners) {
        this.owners = owners;
    }

    public int getPet_using_common_areas() {
        return pet_using_common_areas;
    }

    public void setPet_using_common_areas(int pet_using_common_areas) {
        this.pet_using_common_areas = pet_using_common_areas;
    }
}
