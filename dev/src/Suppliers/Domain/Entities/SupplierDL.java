package Suppliers.Domain.Entities;

import Suppliers.Domain.ValueObjects.PaymentDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.*;

public class SupplierDL implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String businessNumber;
    private final String name;
    private String address;
    private final PaymentDetails paymentDetails;
    private final List<ContactPersonDL> contactPersonnel;
    private final List<AgreementDL> agreements;
    private final List<String> manufacturers;

    public SupplierDL(String name, String businessNumber, String address, PaymentDetails paymentDetails) {
        this.name = name;
        this.businessNumber = businessNumber;
        this.address = address;
        this.paymentDetails = paymentDetails;
        contactPersonnel = new ArrayList<>();
        agreements = new ArrayList<>();
        manufacturers = new ArrayList<>();
    }

    public ContactPersonDL addContactPerson(String cpName, String phone, String email) {
        ContactPersonDL contactPersonDL = new ContactPersonDL(cpName, phone, email);
        contactPersonnel.add(contactPersonDL);
        return contactPersonDL;
    }

    public void removeContactPerson(String phone) {
        boolean removed = contactPersonnel.removeIf(cp -> cp.getPhone().equals(phone));
        if (!removed) throw new IllegalArgumentException("Contact person not found.");
    }

    public AgreementDL addAgreement(List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        AgreementDL agreement = new AgreementDL(fixedDeliveryDays, supplierTransports);
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

    public void setBankAccount(String iban) {
        paymentDetails.setBankAccount(iban);
    }

    public void setPaymentTerms(String paymentTerms) {
        paymentDetails.setPaymentTerms(paymentTerms);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ContactPersonDL updateContactName(String phone, String newName) {
        ContactPersonDL cp = getContactOrThrow(phone);
        cp.setName(newName);
        return cp;
    }

    public ContactPersonDL updateContactPhone(String oldPhone, String newPhone) {
        ContactPersonDL cp = getContactOrThrow(oldPhone);
        cp.setPhone(newPhone);
        return cp;
    }

    public ContactPersonDL updateContactEmail(String phone, String newEmail) {
        ContactPersonDL cp = getContactOrThrow(phone);
        cp.setEmail(newEmail);
        return cp;
    }

    public AgreementDL getAgreementOrThrow(int agreementId) {
        for (AgreementDL a : agreements)
            if (a.getAgreementId() == agreementId) return a;
        throw new NoSuchElementException("Agreement not found");
    }

    public List<AgreementDL> getOnDemandAgreements() {
        List<AgreementDL> onDemand = new ArrayList<>();
        for (AgreementDL a : agreements) if (a.getDeliveryTerms().isOnDemand()) onDemand.add(a);
        return onDemand;
    }

    private ContactPersonDL getContactOrThrow(String phone) {
        for (ContactPersonDL cp : contactPersonnel)
            if (cp.getPhone().equals(phone)) return cp;
        throw new NoSuchElementException("Contact person not found.");
    }

    public String getName() {
        return name;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public String getAddress() {
        return address;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public List<ContactPersonDL> getContactPersonnel() {
        return Collections.unmodifiableList(contactPersonnel);
    }

    public List<AgreementDL> getAgreements() {
        return Collections.unmodifiableList(agreements);
    }

    public List<String> getManufacturers() {
        return Collections.unmodifiableList(manufacturers);
    }
}