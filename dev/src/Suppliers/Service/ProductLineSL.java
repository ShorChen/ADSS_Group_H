package Suppliers.Service;

import Suppliers.Domain.ProductLineDL;

public class ProductLineSL {
    private final int supplierCatalogId;
    private final String name;
    private final double basePrice;
    private final int quantity;

    ProductLineSL(ProductLineDL productLineDL) {
        supplierCatalogId = productLineDL.getSupplierCatalogId();
        name = productLineDL.getName();
        basePrice = productLineDL.getBasePrice();
        quantity = productLineDL.getQuantity();
    }

    public int getSupplierCatalogId() { return supplierCatalogId; }
    public String getName() { return name; }
    public double getBasePrice() { return basePrice; }
    public int getQuantity() { return quantity; }
}