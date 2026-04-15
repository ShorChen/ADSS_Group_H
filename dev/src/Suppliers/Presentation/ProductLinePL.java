package Suppliers.Presentation;

public class ProductLinePL {
    private final int internalCatalogId;
    private final int supplierCatalogId;
    private final double agreedPrice;

    public ProductLinePL(int internalCatalogId, int supplierCatalogId, double agreedPrice) {
        this.internalCatalogId = internalCatalogId;
        this.supplierCatalogId = supplierCatalogId;
        this.agreedPrice = agreedPrice;
    }

    public int getInternalCatalogId() { return internalCatalogId; }
    public int getSupplierCatalogId() { return supplierCatalogId; }
    public double getAgreedPrice() { return agreedPrice; }
}