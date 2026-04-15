package Suppliers.Domain.Business;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeliveryTermsBL {
    private List<DayOfWeek> fixedDeliveryDays;
    private boolean supplierTransports;

    public DeliveryTermsBL(List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        this.fixedDeliveryDays = fixedDeliveryDays != null ? new ArrayList<>(fixedDeliveryDays) : new ArrayList<>();
        this.supplierTransports = supplierTransports;
    }

    public void updateTerms(List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        this.fixedDeliveryDays = fixedDeliveryDays != null ? new ArrayList<>(fixedDeliveryDays) : new ArrayList<>();
        this.supplierTransports = supplierTransports;
    }

    public List<DayOfWeek> getFixedDeliveryDays() { return Collections.unmodifiableList(fixedDeliveryDays); }
    public boolean isSupplierTransports() { return supplierTransports; }
}