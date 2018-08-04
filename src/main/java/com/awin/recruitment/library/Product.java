package com.awin.recruitment.library;

import java.math.BigDecimal;

public class Product {

    public Product(String name, BigDecimal totalCost) {
        this.name = name;
        this.totalCost = totalCost;
    }

    private String name;
    private BigDecimal totalCost;

    public String getName() {
        return this.name;
    }

    public BigDecimal getTotalCost() {
        return this.totalCost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Product)) return false;
        final Product other = (Product) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$totalCost = this.getTotalCost();
        final Object other$totalCost = other.getTotalCost();
        if (this$totalCost == null ? other$totalCost != null : !this$totalCost.equals(other$totalCost)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $totalCost = this.getTotalCost();
        result = result * PRIME + ($totalCost == null ? 43 : $totalCost.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Product;
    }

    public String toString() {
        return "Product(name=" + this.getName() + ", totalCost=" + this.getTotalCost() + ")";
    }
}
