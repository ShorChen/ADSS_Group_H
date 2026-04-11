package Suppliers.Service;

import Suppliers.Domain.Agreement;
import Suppliers.Domain.ContactPerson;
import Suppliers.Domain.Manufacturer;
import Suppliers.Domain.SupplierFacade;

import java.util.List;
import java.util.Map;

public class SupplierService {
    private final SupplierFacade supplierFacade;

    public SupplierService(SupplierFacade supplierFacade) {
        this.supplierFacade = supplierFacade;
    }

    public void addSupplier(String name, List<ContactPerson> contactPersonnel,
                            List<Manufacturer> manufacturers, List<Agreement> agreements, Map<Integer,
                    Integer> stock,  String IBAN, String paymentTerms) {
        supplierFacade.addSupplier(name, contactPersonnel, manufacturers, agreements, stock, IBAN, paymentTerms);
    }

    public void removeSupplier(int id) {
        supplierFacade.removeSupplier(id);
    }

    public void addStock(String name, int catalogNumber, int amount) {
        supplierFacade.addStock(name, catalogNumber, amount);
    }
}
