package Suppliers.Service.DTO;

public record OrderItemSL(
        String productName,
        String supplierBusinessNumber,
        String supplierName,
        int catalogId,
        int quantity,
        double priceBeforeDiscount,
        double finalPrice
) {}