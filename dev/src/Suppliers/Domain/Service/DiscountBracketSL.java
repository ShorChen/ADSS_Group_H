package Suppliers.Domain.Service;

import Suppliers.Domain.Business.DiscountBracketBL;

public class DiscountBracketSL {
    private final int minQuantity;
    private final double discountPercentage;

    DiscountBracketSL(DiscountBracketBL discountBracketBL) {
        this.minQuantity = discountBracketBL.getMinQuantity();
        this.discountPercentage = discountBracketBL.getDiscountPercentage();
    }

    public int getMinQuantity() { return minQuantity; }
    public double getDiscountPercentage() { return discountPercentage; }
}