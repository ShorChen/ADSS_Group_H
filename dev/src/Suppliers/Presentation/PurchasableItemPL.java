package Suppliers.Presentation;

public class PurchasableItemPL {
    private final String productName;
    private final int supplierCatalogId;
    private final double basePrice;
    private final int quantity;
    private final double finalPrice;

    public PurchasableItemPL(String productName, int supplierCatalogId, double basePrice, int quantity, double finalPrice) {
        this.productName = productName;
        this.supplierCatalogId = supplierCatalogId;
        this.basePrice = basePrice;
        this.quantity = quantity;
        this.finalPrice = finalPrice;
    }

    public String getProductName() { return productName; }
    public int getSupplierCatalogId() { return supplierCatalogId; }
    public double getBasePrice() { return basePrice; }
    public int getQuantity() { return quantity; }
    public double getFinalPrice() { return finalPrice; }
}