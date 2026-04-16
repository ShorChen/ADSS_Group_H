package Suppliers.Domain.Service;

import Suppliers.Domain.Business.ProductLineBL;

public class ProductLineSL {
    private final int supplierCatalogId;
    private final String name;
    private final double basePrice;
    private final int quantity;

    ProductLineSL(ProductLineBL productLineBL) {
        this.supplierCatalogId = productLineBL.getSupplierCatalogId();
        this.name = productLineBL.getName();
        this.basePrice = productLineBL.getBasePrice();
        this.quantity = productLineBL.getQuantity();
    }

    public int getSupplierCatalogId() { return supplierCatalogId; }
    public String getName() { return name; }
    public double getBasePrice() { return basePrice; }
    public int getQuantity() { return quantity; }
}