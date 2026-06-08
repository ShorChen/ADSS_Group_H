package Suppliers.Domain.ValueObjects;

import java.io.Serial;
import java.io.Serializable;

public class DiscountBracketDL implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int minQuantity;
    private double discountPercentage;

    DiscountBracketDL(int minQuantity, double discountPercentage) {
        this.minQuantity = minQuantity;
        this.discountPercentage = discountPercentage;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}