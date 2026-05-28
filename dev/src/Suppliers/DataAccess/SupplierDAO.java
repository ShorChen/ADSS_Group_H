package Suppliers.DataAccess;

import Suppliers.Domain.Entities.SupplierDL;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public interface SupplierDAO {
    void addSupplier(SupplierDL supplier);
    void updateSupplier(SupplierDL supplier);
    void deleteSupplier(String businessNumber);
    SupplierDL getSupplier(String businessNumber);
    Map<String, SupplierDL> getAllSuppliers();

    // Contact Persons
    void addContactPersonToDb(String businessNumber, String name, String phone, String email);
    void removeContactPersonFromDb(String phone);
    void updateContactNameInDb(String phone, String newName);
    void updateContactPhoneInDb(String oldPhone, String newPhone);
    void updateContactEmailInDb(String phone, String newEmail);

    // Agreements
    int addAgreementToDb(String businessNumber, List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports);
    void removeAgreementFromDb(int agreementId);
    void updateAgreementDaysInDb(int agreementId, List<DayOfWeek> fixedDeliveryDays);
    void updateAgreementTransportsInDb(int agreementId, boolean transports);

    // Product Lines
    void addProductLineToDb(int agreementId, int catalogId, String name, double basePrice, int quantity);
    void removeProductLineFromDb(int agreementId, int catalogId);
    void updateProductLineBasePriceInDb(int agreementId, int catalogId, double newPrice);
    void updateProductLineQuantityInDb(int agreementId, int catalogId, int newQuantity);

    // Discounts
    void addDiscountToDb(int agreementId, int catalogId, int minQuantity, double discountPercentage);
    void removeDiscountFromDb(int agreementId, int catalogId, int minQuantity);
    void updateDiscountInDb(int agreementId, int catalogId, int minQuantity, double newDiscountPercentage);

    // Manufacturers
    void addManufacturerToDb(String businessNumber, String manufacturer);
    void removeManufacturerFromDb(String businessNumber, String manufacturer);
}