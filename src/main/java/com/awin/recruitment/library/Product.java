package com.awin.recruitment.library;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
public class Product {

    public Product(String name, BigDecimal totalCost) {
        this.name = name;
        this.totalCost = totalCost;
    }

    private String name;
    private BigDecimal totalCost;

}
