package org.example.errors;

import java.math.BigDecimal;

public class InvalidValueForAmountException extends Throwable {
    public InvalidValueForAmountException(BigDecimal value) {
        super("Invalid amount to pay tax " + value );
    }
}
