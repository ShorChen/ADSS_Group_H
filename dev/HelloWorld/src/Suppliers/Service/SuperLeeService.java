package Suppliers.Service;

import Suppliers.Domain.SupplierFacade;

public class SuperLeeService {
    private final SupplierFacade supplierFacade;

    public SuperLeeService(SupplierFacade supplierFacade) {
        this.supplierFacade = supplierFacade;
    }
}
