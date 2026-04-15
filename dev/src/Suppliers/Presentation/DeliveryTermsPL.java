package Suppliers.Presentation;

import java.time.DayOfWeek;
import java.util.List;

public class DeliveryTermsPL {
    private final List<DayOfWeek> fixedDeliveryDays;
    private final boolean supplierTransports;

    public DeliveryTermsPL(List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        this.fixedDeliveryDays = fixedDeliveryDays;
        this.supplierTransports = supplierTransports;
    }

    public List<DayOfWeek> getFixedDeliveryDays() { return fixedDeliveryDays; }
    public boolean isSupplierTransports() { return supplierTransports; }
}