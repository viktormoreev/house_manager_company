package org.example.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Apartment;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentDTO {
    private Long id;
    private int number;
    private BigDecimal area;
    public ApartmentDTO(Apartment apartment) {
        this.id = apartment.getId();
        this.number = apartment.getNumber();
        this.area = apartment.getArea();
    }
    @Override
    public String toString() {
        return "ApartmentDTO{" +
                "id=" + id +
                ", number=" + number +
                ", area=" + area +
                '}';
    }
}
