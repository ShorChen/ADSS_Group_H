package Suppliers.Domain.Service;

public class PurchasableItemSL {
    private final String productName;
    private final int supplierCatalogId;
    private final double price;

    PurchasableItemSL(String productName, int supplierCatalogId, double price) {
        this.productName = productName;
        this.supplierCatalogId = supplierCatalogId;
        this.price = price;
    }

    public String getProductName() { return productName; }
    public int getSupplierCatalogId() { return supplierCatalogId; }
    public double getPrice() { return price; }
}