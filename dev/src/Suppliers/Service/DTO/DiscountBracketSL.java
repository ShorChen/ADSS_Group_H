package Suppliers.Service.DTO;

import Suppliers.Domain.ValueObjects.DiscountBracketDL;

public record DiscountBracketSL(
        int minQuantity,
        double discountPercentage
) {
    public DiscountBracketSL(DiscountBracketDL discountBracketDL) {
        this(
                discountBracketDL.getMinQuantity(),
                discountBracketDL.getDiscountPercentage()
        );
    }
}