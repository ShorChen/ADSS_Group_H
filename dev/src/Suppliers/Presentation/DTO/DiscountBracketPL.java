package Suppliers.Presentation.DTO;

public class DiscountBracketPL {
    private final int minQuantity;
    private final double discountPercentage;

    public DiscountBracketPL(int minQuantity, double discountPercentage) {
        this.minQuantity = minQuantity;
        this.discountPercentage = discountPercentage;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }
}