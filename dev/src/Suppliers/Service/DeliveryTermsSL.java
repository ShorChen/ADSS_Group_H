package Suppliers.Service;

import Suppliers.Domain.DeliveryTermsDL;
import java.time.DayOfWeek;
import java.util.List;

public class DeliveryTermsSL {
    private final List<DayOfWeek> fixedDeliveryDays;
    private final boolean supplierTransports;

    DeliveryTermsSL(DeliveryTermsDL deliveryTermsDL) {
        fixedDeliveryDays = deliveryTermsDL.getFixedDeliveryDays();
        supplierTransports = deliveryTermsDL.isSupplierTransports();
    }

    public List<DayOfWeek> getFixedDeliveryDays() { return fixedDeliveryDays; }
    public boolean isSupplierTransports() { return supplierTransports; }
}