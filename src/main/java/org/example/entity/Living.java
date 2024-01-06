package org.example.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Living extends IdGenerator {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "use_elevator", nullable = false)
    private boolean use_elevator;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate date_of_birth;

    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;

    public Living(String name, boolean use_elevator, LocalDate date_of_birth) {
        this.name = name;
        this.use_elevator = use_elevator;
        this.date_of_birth = date_of_birth;
    }
}
