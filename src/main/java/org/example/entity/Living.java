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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Apartment apartment;


}
