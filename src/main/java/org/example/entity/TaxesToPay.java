package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "taxes_to_pay")
public class TaxesToPay extends IdGenerator{

    private LocalDate localDate;
    private BigDecimal toPay;
    private BigDecimal payed;
    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;

}
