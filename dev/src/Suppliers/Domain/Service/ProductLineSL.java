package Suppliers.Domain.Service;

import Suppliers.Domain.Business.ProductLineBL;

public class ProductLineSL {
    private final int supplierCatalogId;
    private final String name;
    private final double agreedPrice;

    ProductLineSL(ProductLineBL productLineBL) {
        this.supplierCatalogId = productLineBL.getSupplierCatalogId();
        this.name = productLineBL.getName();
        this.agreedPrice = productLineBL.getAgreedPrice();
    }

    public int getSupplierCatalogId() { return supplierCatalogId; }
    public String getName() { return name; }
    public double getAgreedPrice() { return agreedPrice; }
}