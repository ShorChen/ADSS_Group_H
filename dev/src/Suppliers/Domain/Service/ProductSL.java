package Suppliers.Domain.Service;

import Suppliers.Domain.Business.ProductBL;

public class ProductSL {
    private final int catalogNumber;
    private final String name;

    ProductSL(ProductBL product) {
        this.catalogNumber = product.catalogNumber();
        this.name = product.name();
    }

    public int catalogNumber() { return catalogNumber; }
    public String name() { return name; }
}
