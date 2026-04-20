package Suppliers.Service.DTO;

import Suppliers.Domain.ValueObjects.DeliveryTermsDL;

import java.time.DayOfWeek;
import java.util.List;

public record DeliveryTermsSL(
        List<DayOfWeek> fixedDeliveryDays,
        boolean supplierTransports
) {
    public DeliveryTermsSL(DeliveryTermsDL deliveryTermsDL) {
        this(
                deliveryTermsDL.getFixedDeliveryDays(),
                deliveryTermsDL.isSupplierTransports()
        );
    }
}