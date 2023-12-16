package org.example.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

}
