package Suppliers.Presentation;

public class ProductLinePL {
    private final int supplierCatalogId;
    private final String name;
    private final double basePrice;
    private final int quantity;

    public ProductLinePL(int supplierCatalogId, String name, double basePrice, int quantity) {
        this.supplierCatalogId = supplierCatalogId;
        this.name = name;
        this.basePrice = basePrice;
        this.quantity = quantity;
    }

    public int getSupplierCatalogId() { return supplierCatalogId; }
    public String getName() { return name; }
    public double getBasePrice() { return basePrice; }
    public int getQuantity() { return quantity; }
}