package Suppliers.Service.DTO;

import Suppliers.Domain.Entities.ProductLineDL;

public class PurchasableItemSL {
    private final String productName;
    private final int supplierCatalogId;
    private final double basePrice;
    private final int quantity;
    private final double finalPrice;

    public PurchasableItemSL(ProductLineDL pl, double finalPrice) {
        productName = pl.getName();
        supplierCatalogId = pl.getSupplierCatalogId();
        basePrice = pl.getBasePrice();
        quantity = pl.getQuantity();
        this.finalPrice = finalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public int getSupplierCatalogId() {
        return supplierCatalogId;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getFinalPrice() {
        return finalPrice;
    }
}