package Suppliers.Domain.ValueObjects;

public record OrderItemDL(int catalogId, String productName, int quantity, double listPrice, double discount,
                          double finalPrice) {
}