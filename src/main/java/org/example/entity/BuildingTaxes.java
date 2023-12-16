package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BuildingTaxes extends IdGenerator {

    private BigDecimal per_square_footage;

    private BigDecimal per_living;

    private BigDecimal per_pet;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", unique = true)
    private Building building;

}
