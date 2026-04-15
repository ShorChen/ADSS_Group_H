package Suppliers.Domain.Business;

public class ProductLineBL {
    private final int supplierCatalogId;
    private final String name;
    private double agreedPrice;

    ProductLineBL(int supplierCatalogId, String name, double agreedPrice) {
        this.supplierCatalogId = supplierCatalogId;
        this.name = name;
        this.agreedPrice = agreedPrice;
    }

    public int getSupplierCatalogId() { return supplierCatalogId; }
    public String getName() { return name; }
    public double getAgreedPrice() { return agreedPrice; }
    public void setAgreedPrice(double agreedPrice) { this.agreedPrice = agreedPrice; }
}