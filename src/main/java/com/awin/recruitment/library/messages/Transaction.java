package com.awin.recruitment.library.messages;

import java.time.LocalDate;
import java.util.ArrayList;

public class Transaction {

    public Transaction(long idNumber, LocalDate saleDate, Iterable<Product> products) {
        this.idNumber = idNumber;
        this.saleDate = saleDate;
        this.products = products;
    }

    private long idNumber;
    private LocalDate saleDate;
    private Iterable<Product> products;

    public long getIdNumber() {
        return this.idNumber;
    }

    public LocalDate getSaleDate() {
        return this.saleDate;
    }

    public Iterable<Product> getProducts() {
        return this.products;
    }

    public void setIdNumber(long idNumber) {
        this.idNumber = idNumber;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Transaction)) return false;
        final Transaction other = (Transaction) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getIdNumber() != other.getIdNumber()) return false;
        final Object this$saleDate = this.getSaleDate();
        final Object other$saleDate = other.getSaleDate();
        if (this$saleDate == null ? other$saleDate != null : !this$saleDate.equals(other$saleDate)) return false;
        final Object this$products = this.getProducts();
        final Object other$products = other.getProducts();
        if (this$products == null ? other$products != null : !this$products.equals(other$products)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $idNumber = this.getIdNumber();
        result = result * PRIME + (int) ($idNumber >>> 32 ^ $idNumber);
        final Object $saleDate = this.getSaleDate();
        result = result * PRIME + ($saleDate == null ? 43 : $saleDate.hashCode());
        final Object $products = this.getProducts();
        result = result * PRIME + ($products == null ? 43 : $products.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Transaction;
    }

    public String toString() {
        return "Transaction(idNumber=" + this.getIdNumber() + ", saleDate=" + this.getSaleDate() + ", products=" + this.getProducts() + ")";
    }
}
