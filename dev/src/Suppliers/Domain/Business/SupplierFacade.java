package Suppliers.Domain.Business;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class SupplierFacade {
    private final Map<String, SupplierBL> suppliers;

    public SupplierFacade() {
        this.suppliers = new HashMap<>();
    }

    public SupplierBL addSupplier(String name, String businessNumber, String address, String iban, String paymentTerms) {
        stringValidation(name, "Name");
        businessNumberValidation(businessNumber);
        stringValidation(address, "Address");
        stringValidation(iban, "IBAN");
        stringValidation(paymentTerms, "paymentTerms");
        if (suppliers.containsKey(businessNumber))
            throw new IllegalArgumentException("A supplier with this business number already exists");
        SupplierBL supplierBL = new SupplierBL(name, businessNumber, address, new PaymentDetails(iban, paymentTerms));
        suppliers.put(businessNumber, supplierBL);
        return supplierBL;
    }

    public boolean deleteSupplier(String businessNumber) {
        getSupplierOrThrow(businessNumber);
        suppliers.remove(businessNumber);
        return true;
    }

    public ContactPersonBL addContactPerson(String businessNumber, String cpName, String phone, String email) {
        stringValidation(cpName, "Contact Name");
        stringValidation(phone, "Phone");
        return getSupplierOrThrow(businessNumber).addContactPerson(cpName, phone, email);
    }

    public boolean removeContactPerson(String businessNumber, String phone) {
        getSupplierOrThrow(businessNumber).removeContactPerson(phone);
        return true;
    }

    public AgreementBL addAgreement(String businessNumber, List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        return getSupplierOrThrow(businessNumber).addAgreement(fixedDeliveryDays, supplierTransports);
    }

    public boolean removeAgreement(String businessNumber, int agreementId) {
        getSupplierOrThrow(businessNumber).removeAgreement(agreementId);
        return true;
    }

    public ProductLineBL addProductLine(String businessNumber, int agreementId, int supplierCatalogId, String name, double basePrice, int quantity) {
        stringValidation(name, "Product Name");
        return getSupplierOrThrow(businessNumber).getAgreementOrThrow(agreementId).addProductLine(supplierCatalogId, name, basePrice, quantity);
    }

    public boolean removeProductLine(String businessNumber, int agreementId, int supplierCatalogId) {
        getSupplierOrThrow(businessNumber).getAgreementOrThrow(agreementId).removeProductLine(supplierCatalogId);
        return true;
    }

    public ProductLineBL updateProductLineBasePrice(String businessNumber, int agreementId, int supplierCatalogId, double newBasePrice) {
        return getSupplierOrThrow(businessNumber).getAgreementOrThrow(agreementId).updateProductLineBasePrice(supplierCatalogId, newBasePrice);
    }

    public ProductLineBL updateProductLineQuantity(String businessNumber, int agreementId, int supplierCatalogId, int newQuantity) {
        return getSupplierOrThrow(businessNumber).getAgreementOrThrow(agreementId).updateProductLineQuantity(supplierCatalogId, newQuantity);
    }

    public boolean addDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity, double discountPercentage) {
        getSupplierOrThrow(businessNumber).getAgreementOrThrow(agreementId).addDiscount(supplierCatalogId, minQuantity, discountPercentage);
        return true;
    }

    public boolean removeDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity) {
        getSupplierOrThrow(businessNumber).getAgreementOrThrow(agreementId).removeDiscount(supplierCatalogId, minQuantity);
        return true;
    }

    public boolean updateDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity, double newDiscountPercentage) {
        getSupplierOrThrow(businessNumber).getAgreementOrThrow(agreementId).updateDiscount(supplierCatalogId, minQuantity, newDiscountPercentage);
        return true;
    }

    public boolean updateFixedDeliveryDays(String businessNumber, int agreementId, List<DayOfWeek> fixedDeliveryDays) {
        getSupplierOrThrow(businessNumber).getAgreementOrThrow(agreementId).updateFixedDeliveryDays(fixedDeliveryDays);
        return true;
    }

    public boolean updateSupplierTransports(String businessNumber, int agreementId, boolean supplierTransports) {
        getSupplierOrThrow(businessNumber).getAgreementOrThrow(agreementId).updateSupplierTransports(supplierTransports);
        return true;
    }

    public boolean updateBankAccount(String businessNumber, String newIban) {
        stringValidation(newIban, "IBAN");
        getSupplierOrThrow(businessNumber).setBankAccount(newIban);
        return true;
    }

    public boolean updatePaymentTerms(String businessNumber, String newTerms) {
        stringValidation(newTerms, "Payment Terms");
        getSupplierOrThrow(businessNumber).setPaymentTerms(newTerms);
        return true;
    }

    public boolean updateAddress(String businessNumber, String newAddress) {
        stringValidation(newAddress, "Address");
        getSupplierOrThrow(businessNumber).setAddress(newAddress);
        return true;
    }

    public boolean addManufacturer(String businessNumber, String manufacturer) {
        stringValidation(manufacturer, "Manufacturer");
        getSupplierOrThrow(businessNumber).addManufacturer(manufacturer);
        return true;
    }

    public boolean removeManufacturer(String businessNumber, String manufacturer) {
        getSupplierOrThrow(businessNumber).removeManufacturer(manufacturer);
        return true;
    }

    public ContactPersonBL updateContactName(String businessNumber, String phone, String newName) {
        stringValidation(newName, "New Name");
        return getSupplierOrThrow(businessNumber).updateContactName(phone, newName);
    }

    public ContactPersonBL updateContactPhone(String businessNumber, String oldPhone, String newPhone) {
        stringValidation(newPhone, "New Phone");
        return getSupplierOrThrow(businessNumber).updateContactPhone(oldPhone, newPhone);
    }

    public ContactPersonBL updateContactEmail(String businessNumber, String phone, String newEmail) {
        stringValidation(newEmail, "New Email");
        return getSupplierOrThrow(businessNumber).updateContactEmail(phone, newEmail);
    }

    public SupplierBL getSupplier(String businessNumber) {
        return getSupplierOrThrow(businessNumber);
    }

    public List<SupplierBL> getAllSuppliers() {
        return List.copyOf(suppliers.values());
    }

    private SupplierBL getSupplierOrThrow(String businessNumber) {
        businessNumberValidation(businessNumber);
        SupplierBL supplierBL = suppliers.get(businessNumber);
        if (supplierBL == null) throw new NoSuchElementException("This supplier does not exist.");
        return supplierBL;
    }

    private void stringValidation(String str, String name) {
        if (str == null || str.trim().isEmpty()) throw new IllegalArgumentException(name + " Cannot be empty");
    }

    private void businessNumberValidation(String businessNumber) {
        if (businessNumber == null || businessNumber.length() != 9 || !businessNumber.matches("\\d+"))
            throw new IllegalArgumentException("businessNumber must be exactly 9 numeric digits.");
    }
}