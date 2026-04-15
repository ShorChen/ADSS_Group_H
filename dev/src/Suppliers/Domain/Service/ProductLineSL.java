package Suppliers.Domain.Service;

import Suppliers.Domain.Business.ProductLineBL;

public class ProductLineSL {
    private final int internalCatalogId;
    private final int supplierCatalogId;
    private final double agreedPrice;

    ProductLineSL(ProductLineBL productLineBL) {
        this.internalCatalogId = productLineBL.getInternalCatalogId();
        this.supplierCatalogId = productLineBL.getSupplierCatalogId();
        this.agreedPrice = productLineBL.getAgreedPrice();
    }

    public int getInternalCatalogId() { return internalCatalogId; }
    public int getSupplierCatalogId() { return supplierCatalogId; }
    public double getAgreedPrice() { return agreedPrice; }
}