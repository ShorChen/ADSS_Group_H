package Suppliers.Presentation.DTO;

import java.time.LocalDate;
import java.util.List;

public record OrderPL(
        int orderId,
        String supplierBusinessNumber,
        String supplierName,
        String address,
        String contactPhone,
        LocalDate orderDate,
        List<OrderItemPL> items
) {}