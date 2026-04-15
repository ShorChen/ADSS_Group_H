package Suppliers.Domain.Business;

public class ProductLineBL {
    private final int internalCatalogId;
    private final int supplierCatalogId;
    private double agreedPrice;

    public ProductLineBL(int internalCatalogId, int supplierCatalogId, double agreedPrice) {
        this.internalCatalogId = internalCatalogId;
        this.supplierCatalogId = supplierCatalogId;
        this.agreedPrice = agreedPrice;
    }

    public int getInternalCatalogId() { return internalCatalogId; }
    public int getSupplierCatalogId() { return supplierCatalogId; }
    public double getAgreedPrice() { return agreedPrice; }
    public void setAgreedPrice(double agreedPrice) { this.agreedPrice = agreedPrice; }
}