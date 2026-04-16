package Suppliers.Domain;

import java.io.Serial;
import java.io.Serializable;

public class ProductLineDL implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int supplierCatalogId;
    private final String name;
    private double basePrice;
    private int quantity;

    ProductLineDL(int supplierCatalogId, String name, double basePrice, int quantity) {
        this.supplierCatalogId = supplierCatalogId;
        this.name = name;
        this.basePrice = basePrice;
        this.quantity = quantity;
    }

    public int getSupplierCatalogId() { return supplierCatalogId; }
    public String getName() { return name; }
    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}