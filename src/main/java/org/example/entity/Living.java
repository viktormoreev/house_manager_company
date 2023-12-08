package org.example.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Living extends IdGenerator {
    @Column(name = "name")
    private String name;

    @Column(name = "use_elevator")
    private boolean use_elevator;

    @Column(name = "date_of_birth")
    private Date date_of_birth;

    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;
}
