package Suppliers.Domain.ValueObjects;

import java.io.Serial;
import java.io.Serializable;

public class OrderItemDL implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int catalogId;
    private final String productName;
    private final int quantity;
    private final double listPrice;
    private final double discount;
    private final double finalPrice;

    public OrderItemDL(int catalogId, String productName, int quantity, double listPrice, double discount, double finalPrice) {
        this.catalogId = catalogId;
        this.productName = productName;
        this.quantity = quantity;
        this.listPrice = listPrice;
        this.discount = discount;
        this.finalPrice = finalPrice;
    }

    public int getCatalogId() {
        return catalogId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getListPrice() {
        return listPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }
}