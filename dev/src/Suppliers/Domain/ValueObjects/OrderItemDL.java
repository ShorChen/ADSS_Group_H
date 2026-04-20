package Suppliers.Domain.ValueObjects;

import java.io.Serial;
import java.io.Serializable;

public record OrderItemDL(int catalogId, String productName, int quantity, double listPrice, double discount,
                          double finalPrice) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}