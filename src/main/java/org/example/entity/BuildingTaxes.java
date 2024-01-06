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
@Table(name = "building_taxes")
public class BuildingTaxes extends IdGenerator {

    private BigDecimal per_square_footage;

    private BigDecimal per_living;

    private BigDecimal per_pet;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", unique = true)
    private Building building;

    public BuildingTaxes(BigDecimal per_square_footage, BigDecimal per_living, BigDecimal per_pet) {
        this.per_square_footage = per_square_footage;
        this.per_living = per_living;
        this.per_pet = per_pet;
    }

    @Override
    public String toString() {
        return "BuildingTaxes{" +
                "per_square_footage=" + per_square_footage +
                ", per_living=" + per_living +
                ", per_pet=" + per_pet +
                "} " + super.toString();
    }
}
