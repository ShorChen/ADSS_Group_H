package Suppliers.Domain.Facades;

import Suppliers.DataAccess.SupplierDAO;
import Suppliers.Domain.Entities.AgreementDL;
import Suppliers.Domain.Entities.ContactPersonDL;
import Suppliers.Domain.Entities.ProductLineDL;
import Suppliers.Domain.Entities.SupplierDL;
import Suppliers.Domain.ValueObjects.PaymentDetails;
import Suppliers.Domain.ValidationUtils;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class SupplierFacade {
    private final Map<String, SupplierDL> suppliers;
    private final SupplierDAO supplierDAO;

    public SupplierFacade(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
        this.suppliers = supplierDAO.getAllSuppliers();
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
        supplierDAO.addSupplier(supplierDL);
        suppliers.put(businessNumber, supplierDL);
        return supplierDL;
    }

    public boolean deleteSupplier(String businessNumber) {
        getSupplierOrThrow(businessNumber);
        supplierDAO.deleteSupplier(businessNumber);
        suppliers.remove(businessNumber);
        return true;
    }

    public ContactPersonDL addContactPerson(String businessNumber, String cpName, String phone, String email) {
        stringValidation(cpName, "Contact Name");
        stringValidation(phone, "Phone");
        ValidationUtils.validatePhone(phone);
        ValidationUtils.validateEmail(email);
        SupplierDL supplier = getSupplierOrThrow(businessNumber);

        supplierDAO.addContactPersonToDb(businessNumber, cpName, phone, email);
        return supplier.addContactPerson(cpName, phone, email);
    }

    public boolean removeContactPerson(String businessNumber, String phone) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplierDAO.removeContactPersonFromDb(phone);
        supplier.removeContactPerson(phone);
        return true;
    }

    public AgreementDL addAgreement(String businessNumber, List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        int newId = supplierDAO.addAgreementToDb(businessNumber, fixedDeliveryDays, supplierTransports);
        return supplier.addAgreement(newId, fixedDeliveryDays, supplierTransports);
    }

    public boolean removeAgreement(String businessNumber, int agreementId) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplierDAO.removeAgreementFromDb(agreementId);
        supplier.removeAgreement(agreementId);
        return true;
    }

    public ProductLineDL addProductLine(String businessNumber, int agreementId, int supplierCatalogId, String name, double basePrice, int quantity) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        AgreementDL agreement = supplier.getAgreementOrThrow(agreementId);
        supplierDAO.addProductLineToDb(agreementId, supplierCatalogId, name, basePrice, quantity);
        return agreement.addProductLine(supplierCatalogId, name, basePrice, quantity);
    }

    public boolean removeProductLine(String businessNumber, int agreementId, int supplierCatalogId) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplier.getAgreementOrThrow(agreementId); // validation
        supplierDAO.removeProductLineFromDb(agreementId, supplierCatalogId);
        supplier.getAgreementOrThrow(agreementId).removeProductLine(supplierCatalogId);
        return true;
    }

    public ProductLineDL updateProductLineBasePrice(String businessNumber, int agreementId, int supplierCatalogId, double newBasePrice) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplierDAO.updateProductLineBasePriceInDb(agreementId, supplierCatalogId, newBasePrice);
        return supplier.getAgreementOrThrow(agreementId).updateProductLineBasePrice(supplierCatalogId, newBasePrice);
    }

    public ProductLineDL updateProductLineQuantity(String businessNumber, int agreementId, int supplierCatalogId, int newQuantity) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplierDAO.updateProductLineQuantityInDb(agreementId, supplierCatalogId, newQuantity);
        return supplier.getAgreementOrThrow(agreementId).updateProductLineQuantity(supplierCatalogId, newQuantity);
    }

    public boolean addDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity, double discountPercentage) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplierDAO.addDiscountToDb(agreementId, supplierCatalogId, minQuantity, discountPercentage);
        supplier.getAgreementOrThrow(agreementId).addDiscount(supplierCatalogId, minQuantity, discountPercentage);
        return true;
    }

    public boolean removeDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplierDAO.removeDiscountFromDb(agreementId, supplierCatalogId, minQuantity);
        supplier.getAgreementOrThrow(agreementId).removeDiscount(supplierCatalogId, minQuantity);
        return true;
    }

    public boolean updateDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity, double newDiscountPercentage) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplierDAO.updateDiscountInDb(agreementId, supplierCatalogId, minQuantity, newDiscountPercentage);
        supplier.getAgreementOrThrow(agreementId).updateDiscount(supplierCatalogId, minQuantity, newDiscountPercentage);
        return true;
    }

    public boolean updateFixedDeliveryDays(String businessNumber, int agreementId, List<DayOfWeek> fixedDeliveryDays) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplierDAO.updateAgreementDaysInDb(agreementId, fixedDeliveryDays);
        supplier.getAgreementOrThrow(agreementId).updateFixedDeliveryDays(fixedDeliveryDays);
        return true;
    }

    public boolean updateSupplierTransports(String businessNumber, int agreementId, boolean supplierTransports) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplierDAO.updateAgreementTransportsInDb(agreementId, supplierTransports);
        supplier.getAgreementOrThrow(agreementId).updateSupplierTransports(supplierTransports);
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
        supplierDAO.addManufacturerToDb(businessNumber, manufacturer);
        supplier.addManufacturer(manufacturer);
        return true;
    }

    public boolean removeManufacturer(String businessNumber, String manufacturer) {
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplierDAO.removeManufacturerFromDb(businessNumber, manufacturer);
        supplier.removeManufacturer(manufacturer);
        return true;
    }

    public ContactPersonDL updateContactName(String businessNumber, String phone, String newName) {
        stringValidation(newName, "New Name");
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplierDAO.updateContactNameInDb(phone, newName);
        return supplier.updateContactName(phone, newName);
    }

    public ContactPersonDL updateContactPhone(String businessNumber, String oldPhone, String newPhone) {
        stringValidation(newPhone, "New Phone");
        ValidationUtils.validatePhone(newPhone);
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplierDAO.updateContactPhoneInDb(oldPhone, newPhone);
        return supplier.updateContactPhone(oldPhone, newPhone);
    }

    public ContactPersonDL updateContactEmail(String businessNumber, String phone, String newEmail) {
        stringValidation(newEmail, "New Email");
        ValidationUtils.validateEmail(newEmail);
        SupplierDL supplier = getSupplierOrThrow(businessNumber);
        supplierDAO.updateContactEmailInDb(phone, newEmail);
        return supplier.updateContactEmail(phone, newEmail);
    }

    public List<SupplierDL> getAllSuppliers() {
        return List.copyOf(suppliers.values());
    }

    public SupplierDL getSupplierOrThrow(String businessNumber) {
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