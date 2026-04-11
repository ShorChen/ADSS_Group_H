package Suppliers.Domain;

import java.time.DayOfWeek;
import java.util.List;

public class DeliveryTerms {
    private final List<DayOfWeek> fixedDeliveryDays;
    private final boolean supplierTransports;

    public DeliveryTerms(List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        this.fixedDeliveryDays = fixedDeliveryDays;
        this.supplierTransports = supplierTransports;
    }

    public List<DayOfWeek> getFixedDeliveryDays() { return fixedDeliveryDays; }
    public boolean isSupplierTransports() { return supplierTransports; }
    public boolean isOnDemand() { return fixedDeliveryDays == null || fixedDeliveryDays.isEmpty(); }

}