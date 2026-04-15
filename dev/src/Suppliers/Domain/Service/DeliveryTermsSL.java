package Suppliers.Domain.Service;

import Suppliers.Domain.Business.DeliveryTermsBL;
import java.time.DayOfWeek;
import java.util.List;

public class DeliveryTermsSL {
    private final List<DayOfWeek> fixedDeliveryDays;
    private final boolean supplierTransports;

    DeliveryTermsSL(DeliveryTermsBL deliveryTermsBL) {
        this.fixedDeliveryDays = deliveryTermsBL.getFixedDeliveryDays();
        this.supplierTransports = deliveryTermsBL.isSupplierTransports();
    }

    public List<DayOfWeek> getFixedDeliveryDays() { return fixedDeliveryDays; }
    public boolean isSupplierTransports() { return supplierTransports; }
}