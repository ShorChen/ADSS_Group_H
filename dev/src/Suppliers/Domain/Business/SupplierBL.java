package Suppliers.Domain.Business;

import java.time.DayOfWeek;
import java.util.*;

public class SupplierBL {
    private final String businessNumber;
    private final PaymentDetails paymentDetails;
    private final List<ContactPersonBL> contactPersonnel;
    private final List<AgreementBL> agreements;

    public SupplierBL(String businessNumber, PaymentDetails paymentDetails) {
        this.businessNumber = businessNumber;
        this.paymentDetails = paymentDetails;
        this.contactPersonnel = new ArrayList<>();
        this.agreements = new ArrayList<>();
    }

    public ContactPersonBL addContactPerson(String cpName, String phone, String email) {
        ContactPersonBL contactPersonBL = new ContactPersonBL(cpName, phone, email);
        contactPersonnel.add(contactPersonBL);
        return contactPersonBL;
    }

    public void removeContactPerson(String phone) {
        boolean removed = contactPersonnel.removeIf(cp -> cp.getPhone().equals(phone));
        if (!removed) throw new IllegalArgumentException("Contact person with this phone number not found.");
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

    public void setBankAccount(String iban) {
        this.paymentDetails.setBankAccount(iban);
    }

    public void setPaymentTerms(String paymentTerms){
        this.paymentDetails.setPaymentTerms(paymentTerms);
    }

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
        for (AgreementBL a : agreements)
            if (a.getAgreementId() == agreementId) return a;
        throw new NoSuchElementException("Agreement not found");
    }

    private ContactPersonBL getContactOrThrow(String phone) {
        for (ContactPersonBL cp : contactPersonnel)
            if (cp.getPhone().equals(phone)) return cp;
        throw new NoSuchElementException("Contact person with phone " + phone + " not found.");
    }

    public String getBusinessNumber() { return businessNumber; }
    public PaymentDetails getPaymentDetails() { return paymentDetails; }
    public List<ContactPersonBL> getContactPersonnel() { return Collections.unmodifiableList(contactPersonnel); }
    public List<AgreementBL> getAgreements() { return Collections.unmodifiableList(agreements); }
}