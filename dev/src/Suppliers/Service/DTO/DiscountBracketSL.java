package Suppliers.Service.DTO;

import Suppliers.Domain.ValueObjects.DiscountBracketDL;

public class DiscountBracketSL {
    private final int minQuantity;
    private final double discountPercentage;

    public DiscountBracketSL(DiscountBracketDL discountBracketDL) {
        minQuantity = discountBracketDL.getMinQuantity();
        discountPercentage = discountBracketDL.getDiscountPercentage();
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }
}