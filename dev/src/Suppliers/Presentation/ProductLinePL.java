package Suppliers.Presentation;

public class ProductLinePL {
    private final int supplierCatalogId;
    private final String name;
    private final double agreedPrice;

    public ProductLinePL(int supplierCatalogId, String name, double agreedPrice) {
        this.supplierCatalogId = supplierCatalogId;
        this.name = name;
        this.agreedPrice = agreedPrice;
    }

    public int getSupplierCatalogId() { return supplierCatalogId; }
    public String getName() { return name; }
    public double getAgreedPrice() { return agreedPrice; }
}