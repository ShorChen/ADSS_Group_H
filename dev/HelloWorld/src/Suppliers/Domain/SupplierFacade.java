package Suppliers.Domain;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public record SupplierFacade(List<Supplier> suppliers) {

    public void addSupplier(String name, List<ContactPerson> contactPersonnel,
                            List<Manufacturer> manufacturers, List<Agreement> agreements, Map<Integer,
                    Integer> stock, String IBAN, String paymentTerms) {
        suppliers.add(new Supplier(name, contactPersonnel, manufacturers, agreements, stock,
                new PaymentDetails(new BankAccount(IBAN), paymentTerms)));
    }

    public void removeSupplier(int id) {
        Iterator<Supplier> it = suppliers.iterator();
        while (it.hasNext())
            if (it.next().getBusinessNumber() == id) {
                it.remove();
                return;
            }
    }

    public void addStock(String name, int catalogNumber, int amount) {
        for (Supplier supplier : suppliers) {
            if (supplier.getName().equals(name)) {
                supplier.addStock(catalogNumber, amount);
                break;
            }
        }
    }
}
