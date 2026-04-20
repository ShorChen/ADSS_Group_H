package Suppliers.Presentation.DTO;

public record OrderItemPL(String productName, String supplierBusinessNumber, String supplierName, int catalogId,
                          int quantity, double priceBeforeDiscount, double totalPrice) {
}