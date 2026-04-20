package Suppliers.Service.DTO;

import Suppliers.Domain.Entities.ProductLineDL;

public record ProductLineSL(
        int supplierCatalogId,
        String name,
        double basePrice,
        int quantity
) {
    public ProductLineSL(ProductLineDL productLineDL) {
        this(
                productLineDL.getSupplierCatalogId(),
                productLineDL.getName(),
                productLineDL.getBasePrice(),
                productLineDL.getQuantity()
        );
    }
}