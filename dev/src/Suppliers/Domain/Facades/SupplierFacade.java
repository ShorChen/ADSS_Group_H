package Suppliers.Domain.Facades;

import Suppliers.DataAccess.SupplierDAO;
import Suppliers.Domain.Entities.AgreementDL;
import Suppliers.Domain.Entities.ContactPersonDL;
import Suppliers.Domain.Entities.ProductLineDL;
import Suppliers.Domain.Entities.SupplierDL;
import Suppliers.Domain.ValueObjects.PaymentDetails;
import Suppliers.Domain.ValidationUtils;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class SupplierFacade {
    private final Map<String, SupplierDL> suppliers;
    private final SupplierDAO supplierDAO;

    public SupplierFacade(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
        this.suppliers = supplierDAO.getAllSuppliers();
        int maxAgreementId = 0;
        for (SupplierDL supplier : suppliers.values())
            for (AgreementDL agreement : supplier.getAgreements())
                if (agreement.getAgreementId() > maxAgreementId)
                    maxAgreementId = agreement.getAgreementId();
        AgreementDL.updateIdCounter(maxAgreementId);
    }

    public SupplierDL addSupplier(String name, String businessNumber, String address, String iban, String paymentTerms) {
        stringValidation(name, "Name");
        businessNumberValidation(businessNumber);
        stringValidation(address, "Address");
        stringValidation(iban, "IBAN");
        stringValidation(paymentTerms, "paymentTerms");
        if (suppliers.containsKey(businessNumber))
            throw new IllegalArgumentException("A supplier with this business number already exists");
        SupplierDL supplierDL = new SupplierDL(name, businessNumber, address, new PaymentDetails(iban, paymentTerms));
        suppliers.put(businessNumber, supplierDL);
        supplierDAO.addSupplier(supplierDL);
        return supplierDL;
    }

    public boolean deleteSupplier(String businessNumber) {
        getSupplierOrThrow(businessNumber);
        suppliers.remove(businessNumber);
        supplierDAO.deleteSupplier(businessNumber);
        return true;
    }

    public ContactPersonDL addContactPerson(String businessNumber, String cpName, String phone, String email) {
        stringValidation(cpName, "Contact Name");
        stringValidation(phone, "Phone");
        ValidationUtils.validatePhone(phone);
        ValidationUtils.validateEmail(email);
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        ContactPersonDL cp = supplier.addContactPerson(cpName, phone, email);
        supplierDAO.updateSupplier(supplier);
        return cp;
    }

    public boolean removeContactPerson(String businessNumber, String phone) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplier.removeContactPerson(phone);
        supplierDAO.updateSupplier(supplier);
        return true;
    }

    public AgreementDL addAgreement(String businessNumber, List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        AgreementDL agreement = supplier.addAgreement(fixedDeliveryDays, supplierTransports);
        supplierDAO.updateSupplier(supplier);
        return agreement;
    }

    public boolean removeAgreement(String businessNumber, int agreementId) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplier.removeAgreement(agreementId);
        supplierDAO.updateSupplier(supplier);
        return true;
    }

    public ProductLineDL addProductLine(String businessNumber, int agreementId, int supplierCatalogId, String name, double basePrice, int quantity) {
        stringValidation(name, "Product Name");
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        ProductLineDL pl = supplier.getAgreementOrThrow(agreementId).addProductLine(supplierCatalogId, name, basePrice, quantity);
        supplierDAO.updateSupplier(supplier);
        return pl;
    }

    public boolean removeProductLine(String businessNumber, int agreementId, int supplierCatalogId) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplier.getAgreementOrThrow(agreementId).removeProductLine(supplierCatalogId);
        supplierDAO.updateSupplier(supplier);
        return true;
    }

    public ProductLineDL updateProductLineBasePrice(String businessNumber, int agreementId, int supplierCatalogId, double newBasePrice) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        ProductLineDL pl = supplier.getAgreementOrThrow(agreementId).updateProductLineBasePrice(supplierCatalogId, newBasePrice);
        supplierDAO.updateSupplier(supplier);
        return pl;
    }

    public ProductLineDL updateProductLineQuantity(String businessNumber, int agreementId, int supplierCatalogId, int newQuantity) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        ProductLineDL pl = supplier.getAgreementOrThrow(agreementId).updateProductLineQuantity(supplierCatalogId, newQuantity);
        supplierDAO.updateSupplier(supplier);
        return pl;
    }

    public boolean addDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity, double discountPercentage) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplier.getAgreementOrThrow(agreementId).addDiscount(supplierCatalogId, minQuantity, discountPercentage);
        supplierDAO.updateSupplier(supplier);
        return true;
    }

    public boolean removeDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplier.getAgreementOrThrow(agreementId).removeDiscount(supplierCatalogId, minQuantity);
        supplierDAO.updateSupplier(supplier);
        return true;
    }

    public boolean updateDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity, double newDiscountPercentage) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplier.getAgreementOrThrow(agreementId).updateDiscount(supplierCatalogId, minQuantity, newDiscountPercentage);
        supplierDAO.updateSupplier(supplier);
        return true;
    }

    public boolean updateFixedDeliveryDays(String businessNumber, int agreementId, List<DayOfWeek> fixedDeliveryDays) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplier.getAgreementOrThrow(agreementId).updateFixedDeliveryDays(fixedDeliveryDays);
        supplierDAO.updateSupplier(supplier);
        return true;
    }

    public boolean updateSupplierTransports(String businessNumber, int agreementId, boolean supplierTransports) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplier.getAgreementOrThrow(agreementId).updateSupplierTransports(supplierTransports);
        supplierDAO.updateSupplier(supplier);
        return true;
    }

    public boolean updateBankAccount(String businessNumber, String newIban) {
        stringValidation(newIban, "IBAN");
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplier.setBankAccount(newIban);
        supplierDAO.updateSupplier(supplier);
        return true;
    }

    public boolean updatePaymentTerms(String businessNumber, String newTerms) {
        stringValidation(newTerms, "Payment Terms");
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplier.setPaymentTerms(newTerms);
        supplierDAO.updateSupplier(supplier);
        return true;
    }

    public boolean updateAddress(String businessNumber, String newAddress) {
        stringValidation(newAddress, "Address");
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplier.setAddress(newAddress);
        supplierDAO.updateSupplier(supplier);
        return true;
    }

    public boolean addManufacturer(String businessNumber, String manufacturer) {
        stringValidation(manufacturer, "Manufacturer");
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplier.addManufacturer(manufacturer);
        supplierDAO.updateSupplier(supplier);
        return true;
    }

    public boolean removeManufacturer(String businessNumber, String manufacturer) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplier.removeManufacturer(manufacturer);
        supplierDAO.updateSupplier(supplier);
        return true;
    }

    public ContactPersonDL updateContactName(String businessNumber, String phone, String newName) {
        stringValidation(newName, "New Name");
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        ContactPersonDL cp = supplier.updateContactName(phone, newName);
        supplierDAO.updateSupplier(supplier);
        return cp;
    }

    public ContactPersonDL updateContactPhone(String businessNumber, String oldPhone, String newPhone) {
        stringValidation(newPhone, "New Phone");
        ValidationUtils.validatePhone(newPhone);
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        ContactPersonDL cp = supplier.updateContactPhone(oldPhone, newPhone);
        supplierDAO.updateSupplier(supplier);
        return cp;
    }

    public ContactPersonDL updateContactEmail(String businessNumber, String phone, String newEmail) {
        stringValidation(newEmail, "New Email");
        ValidationUtils.validateEmail(newEmail);
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        ContactPersonDL cp = supplier.updateContactEmail(phone, newEmail);
        supplierDAO.updateSupplier(supplier);
        return cp;
    }

    public SupplierDL getSupplier(String businessNumber) {
        return getSupplierOrThrow(businessNumber);
    }

    public List<SupplierDL> getAllSuppliers() {
        return List.copyOf(suppliers.values());
    }

    public List<SupplierDL> getOnDemandSuppliers() {
        List<SupplierDL> onDemand = new ArrayList<>();
        for (SupplierDL supplier : suppliers.values())
            if (!supplier.getOnDemandAgreements().isEmpty()) onDemand.add(supplier);
        return onDemand;
    }

    private SupplierDL getSupplierOrThrow(String businessNumber) {
        businessNumberValidation(businessNumber);
        SupplierDL supplierDL = suppliers.get(businessNumber);
        if (supplierDL == null) throw new NoSuchElementException("This supplier does not exist.");
        return supplierDL;
    }

    private void stringValidation(String str, String name) {
        if (str == null || str.trim().isEmpty()) throw new IllegalArgumentException(name + " Cannot be empty");
    }

    private void businessNumberValidation(String businessNumber) {
        if (businessNumber == null || businessNumber.length() != 9 || !businessNumber.matches("\\d+"))
            throw new IllegalArgumentException("businessNumber must be exactly 9 numeric digits.");
    }
}