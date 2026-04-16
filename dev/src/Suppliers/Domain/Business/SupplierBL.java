package Suppliers.Domain.Business;

import java.time.DayOfWeek;
import java.util.*;

public class SupplierBL {
    private final String businessNumber;
    private final String name;
    private String address;
    private final PaymentDetails paymentDetails;
    private final List<ContactPersonBL> contactPersonnel;
    private final List<AgreementBL> agreements;
    private final List<String> manufacturers;

    SupplierBL(String name, String businessNumber, String address, PaymentDetails paymentDetails) {
        this.name = name;
        this.businessNumber = businessNumber;
        this.address = address;
        this.paymentDetails = paymentDetails;
        this.contactPersonnel = new ArrayList<>();
        this.agreements = new ArrayList<>();
        this.manufacturers = new ArrayList<>();
    }

    public ContactPersonBL addContactPerson(String cpName, String phone, String email) {
        ContactPersonBL contactPersonBL = new ContactPersonBL(cpName, phone, email);
        contactPersonnel.add(contactPersonBL);
        return contactPersonBL;
    }

    public void removeContactPerson(String phone) {
        boolean removed = contactPersonnel.removeIf(cp -> cp.getPhone().equals(phone));
        if (!removed) throw new IllegalArgumentException("Contact person not found.");
    }

    public AgreementBL addAgreement(List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        AgreementBL agreement = new AgreementBL(fixedDeliveryDays, supplierTransports);
        agreements.add(agreement);
        return agreement;
    }

    public void removeAgreement(int agreementId) {
        boolean removed = agreements.removeIf(a -> a.getAgreementId() == agreementId);
        if (!removed) throw new NoSuchElementException("Agreement not found");
    }

    public void addManufacturer(String manufacturer) {
        if (!manufacturers.contains(manufacturer)) manufacturers.add(manufacturer);
    }

    public void removeManufacturer(String manufacturer) {
        if (!manufacturers.remove(manufacturer)) throw new NoSuchElementException("Manufacturer not found");
    }

    public void setBankAccount(String iban) { this.paymentDetails.setBankAccount(iban); }
    public void setPaymentTerms(String paymentTerms) { this.paymentDetails.setPaymentTerms(paymentTerms); }
    public void setAddress(String address) { this.address = address; }

    public ContactPersonBL updateContactName(String phone, String newName) {
        ContactPersonBL cp = getContactOrThrow(phone);
        cp.setName(newName);
        return cp;
    }

    public ContactPersonBL updateContactPhone(String oldPhone, String newPhone) {
        ContactPersonBL cp = getContactOrThrow(oldPhone);
        cp.setPhone(newPhone);
        return cp;
    }

    public ContactPersonBL updateContactEmail(String phone, String newEmail) {
        ContactPersonBL cp = getContactOrThrow(phone);
        cp.setEmail(newEmail);
        return cp;
    }

    public AgreementBL getAgreementOrThrow(int agreementId) {
        for (AgreementBL a : agreements) {
            if (a.getAgreementId() == agreementId) return a;
        }
        throw new NoSuchElementException("Agreement not found");
    }

    private ContactPersonBL getContactOrThrow(String phone) {
        for (ContactPersonBL cp : contactPersonnel) {
            if (cp.getPhone().equals(phone)) return cp;
        }
        throw new NoSuchElementException("Contact person not found.");
    }

    public String getName() { return name; }
    public String getBusinessNumber() { return businessNumber; }
    public String getAddress() { return address; }
    public PaymentDetails getPaymentDetails() { return paymentDetails; }
    public List<ContactPersonBL> getContactPersonnel() { return Collections.unmodifiableList(contactPersonnel); }
    public List<AgreementBL> getAgreements() { return Collections.unmodifiableList(agreements); }
    public List<String> getManufacturers() { return Collections.unmodifiableList(manufacturers); }
}