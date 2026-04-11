package Suppliers.Domain;

import java.util.*;

public class Supplier {
    private static int id = 0;
    private final int businessNumber;
    private final String name;
    private final PaymentDetails paymentDetails;
    private final List<ContactPerson> contactPersonnel;
    private final List<Manufacturer> manufacturers;
    private final List<Agreement> agreements;
    private final Map<Integer, Integer> stock;

    public Supplier(String name, List<ContactPerson> contactPersonnel,
                    List<Manufacturer> manufacturers, List<Agreement> agreements, Map<Integer, Integer> stock,
                    PaymentDetails paymentDetails) {
        businessNumber = ++id;
        this.name = name;
        this.contactPersonnel = contactPersonnel;
        this.manufacturers = manufacturers;
        this.agreements = agreements;
        this.stock = stock;
        this.paymentDetails = paymentDetails;
    }

    public String getName() { return name; }
    public int getBusinessNumber() { return businessNumber; }
    public PaymentDetails getPaymentDetails() { return paymentDetails; }
    public List<ContactPerson> getContactPersonnel() { return Collections.unmodifiableList(contactPersonnel); }
    public List<Manufacturer> getManufacturers() { return Collections.unmodifiableList(manufacturers); }
    public List<Agreement> getAgreements() { return Collections.unmodifiableList(agreements); }

    public void setBankAccount(String IBAN) {
        paymentDetails.setBankAccount(IBAN);
    }

    public void setPaymentDetails(String paymentTerms){
        paymentDetails.setPaymentTerms(paymentTerms);
    }

    public void addContactPerson(ContactPerson contactPerson) {
        contactPersonnel.add(contactPerson);
    }

    public void removeContactPerson(String phone) {
        if (phone == null) return;
        Iterator<ContactPerson> it = contactPersonnel.iterator();
        while (it.hasNext())
            if (phone.equals(it.next().getPhone())) {
                it.remove();
                return;
            }
    }

    public void addManufacturer(Manufacturer manufacturer) {
        manufacturers.add(manufacturer);
    }

    public void removeManufacturer(String name) {
        if (name == null) return;
        Iterator<Manufacturer> it = manufacturers.iterator();
        while (it.hasNext())
            if (it.next().name().equals(name)) {
                it.remove();
                return;
            }
    }

    public void addAgreement(Agreement agreement) {
        agreements.add(agreement);
    }

    public void removeAgreement(int id) {
        if (id <= 0) return;
        Iterator<Agreement> it = agreements.iterator();
        while (it.hasNext())
            if (it.next().getId() == id) {
                it.remove();
                return;
            }
    }

    private Optional<Agreement> findAgreementForProduct(int catalogNumber) {
        return agreements.stream()
                .filter(a -> a.getAgreedPrice(catalogNumber) >= 0)
                .findFirst();
    }

    private double getAgreedPrice(int catalogNumber) {
        return findAgreementForProduct(catalogNumber)
                .map(a -> a.getAgreedPrice(catalogNumber))
                .orElse(-1.0);
    }

    private Optional<Integer> getSupplierCatalogNumber(int internalCatalogNumber) {
        return agreements.stream()
                .flatMap(a -> a.getProductLines().stream())
                .filter(pl -> pl.getProduct().catalogNumber() == internalCatalogNumber)
                .map(ProductLine::getSupplierCatalogNumber)
                .findFirst();
    }

    public void addStock(int catalogNumber, int amount) {
        stock.merge(catalogNumber, amount, Integer::sum);
    }
}
