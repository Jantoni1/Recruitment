package com.awin.recruitment.library.messages;

import java.math.BigDecimal;
import java.time.LocalDate;


public class EnrichedTransaction {
    public EnrichedTransaction(Transaction transaction, BigDecimal costSum) {
        this.idNumber = transaction.getIdNumber();
        this.saleDate = transaction.getSaleDate();
        this.products = transaction.getProducts();
        this.costSum = costSum;
    }

    private long idNumber;
    private LocalDate saleDate;
    private Iterable<Product> products;
    private BigDecimal costSum;

    public BigDecimal getCostSum() {
        return this.costSum;
    }

    public void setCostSum(BigDecimal costSum) {
        this.costSum = costSum;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof EnrichedTransaction)) return false;
        final EnrichedTransaction other = (EnrichedTransaction) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$costSum = this.getCostSum();
        final Object other$costSum = other.getCostSum();
        if (this$costSum == null ? other$costSum != null : !this$costSum.equals(other$costSum)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $costSum = this.getCostSum();
        result = result * PRIME + ($costSum == null ? 43 : $costSum.hashCode());
        return result;
    }

    private boolean canEqual(Object other) {
        return other instanceof EnrichedTransaction;
    }

    public String toString() {
        return "EnrichedTransaction(costSum=" + this.getCostSum() + ")";
    }

    public long getIdNumber() {
        return idNumber;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public Iterable<Product> getProducts() {
        return products;
    }

}
