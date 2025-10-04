package com.example.banking.benefit.domain.model.customer;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerAttribute {
    private String stringValue;
    private Integer integerValue;
    private BigDecimal decimalValue;
    private LocalDate dateValue;
    private Boolean booleanValue;
}
