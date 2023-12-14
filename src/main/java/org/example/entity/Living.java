package org.example.entity;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Living extends IdGenerator {

    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "use_elevator", nullable = false)
    private boolean use_elevator;


    @Column(name = "date_of_birth", nullable = false)
    private LocalDate date_of_birth;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Apartment apartment;

    public Living(String name, boolean use_elevator, LocalDate date_of_birth) {
        this.name = name;
        this.use_elevator = use_elevator;
        this.date_of_birth = date_of_birth;
    }

    public Living() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUse_elevator() {
        return use_elevator;
    }

    public void setUse_elevator(boolean use_elevator) {
        this.use_elevator = use_elevator;
    }


    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
}
