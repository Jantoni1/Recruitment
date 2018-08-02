package com.awin.recruitment.library;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
public class Transaction {

    public Transaction(long idNumber, LocalDate saleDate, ArrayList<Product> products) {
        this.idNumber = idNumber;
        this.saleDate = saleDate;
        this.products = products;
    }

    private long idNumber;
    private LocalDate saleDate;
    private ArrayList<Product> products;
}
