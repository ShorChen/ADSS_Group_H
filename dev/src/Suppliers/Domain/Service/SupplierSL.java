package Suppliers.Domain.Service;

import Suppliers.Domain.Business.SupplierBL;

public class SupplierSL {
    private final String businessNumber;

    SupplierSL(SupplierBL supplierBL) {
        this.businessNumber = supplierBL.getBusinessNumber();
    }

    public String getBusinessNumber() { return businessNumber; }
}