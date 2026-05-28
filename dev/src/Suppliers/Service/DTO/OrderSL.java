package Suppliers.Service.DTO;

import java.time.LocalDate;
import java.util.List;

public record OrderSL(
        int orderId,
        String supplierBusinessNumber,
        String supplierName,
        String address,
        String contactPhone,
        LocalDate orderDate,
        List<OrderItemSL> items
) {}