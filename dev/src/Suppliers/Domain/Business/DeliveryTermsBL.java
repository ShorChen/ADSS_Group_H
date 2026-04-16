package Suppliers.Domain.Business;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeliveryTermsBL {
    private List<DayOfWeek> fixedDeliveryDays;
    private boolean supplierTransports;

    DeliveryTermsBL(List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        this.fixedDeliveryDays = fixedDeliveryDays != null ? new ArrayList<>(fixedDeliveryDays) : new ArrayList<>();
        this.supplierTransports = supplierTransports;
    }

    public void updateFixedDeliveryDays(List<DayOfWeek> fixedDeliveryDays) {
        this.fixedDeliveryDays = fixedDeliveryDays != null ? new ArrayList<>(fixedDeliveryDays) : new ArrayList<>();
    }

    public void updateSupplierTransports(boolean supplierTransports) {
        this.supplierTransports = supplierTransports;
    }

    public List<DayOfWeek> getFixedDeliveryDays() { return Collections.unmodifiableList(fixedDeliveryDays); }
    public boolean isSupplierTransports() { return supplierTransports; }
    public boolean isOnDemand() { return fixedDeliveryDays == null || fixedDeliveryDays.isEmpty(); }
}