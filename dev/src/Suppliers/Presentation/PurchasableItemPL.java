package Suppliers.Presentation;

public class PurchasableItemPL {
    private final String productName;
    private final int supplierCatalogId;
    private final double price;

    public PurchasableItemPL(String productName, int supplierCatalogId, double price) {
        this.productName = productName;
        this.supplierCatalogId = supplierCatalogId;
        this.price = price;
    }

    public String getProductName() { return productName; }
    public int getSupplierCatalogId() { return supplierCatalogId; }
    public double getPrice() { return price; }
}