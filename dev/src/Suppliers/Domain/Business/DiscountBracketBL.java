package Suppliers.Domain.Business;

public class DiscountBracketBL {
    private final int minQuantity;
    private double discountPercentage;

    DiscountBracketBL(int minQuantity, double discountPercentage) {
        this.minQuantity = minQuantity;
        this.discountPercentage = discountPercentage;
    }

    public int getMinQuantity() { return minQuantity; }
    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }
}