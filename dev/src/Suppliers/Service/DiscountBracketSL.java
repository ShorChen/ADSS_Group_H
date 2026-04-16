package Suppliers.Service;

import Suppliers.Domain.DiscountBracketDL;

public class DiscountBracketSL {
    private final int minQuantity;
    private final double discountPercentage;

    DiscountBracketSL(DiscountBracketDL discountBracketDL) {
        minQuantity = discountBracketDL.getMinQuantity();
        discountPercentage = discountBracketDL.getDiscountPercentage();
    }

    public int getMinQuantity() { return minQuantity; }
    public double getDiscountPercentage() { return discountPercentage; }
}