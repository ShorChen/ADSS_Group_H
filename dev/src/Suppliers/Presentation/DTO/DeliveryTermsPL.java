package Suppliers.Presentation.DTO;

import java.time.DayOfWeek;
import java.util.List;

public record DeliveryTermsPL(List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
}