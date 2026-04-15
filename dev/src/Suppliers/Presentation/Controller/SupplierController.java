package Suppliers.Presentation.Controller;

import Suppliers.Domain.Service.*;
import Suppliers.Presentation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SupplierController {
    private final SupplierService supplierService;

    SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    public SupplierPL addSupplier(String businessNumber, String iban, String paymentTerms) throws Exception {
        Response<SupplierSL> response = supplierService.addSupplier(businessNumber, iban, paymentTerms);
        if (response.isSuccess()) return new SupplierPL(response.getData().getBusinessNumber());
        throw new Exception(response.getErrorMessage());
    }

    public boolean deleteSupplier(String businessNumber) throws Exception {
        return extractBooleanOrThrow(supplierService.deleteSupplier(businessNumber));
    }

    public ContactPersonPL addContactPerson(String businessNumber, String cpName, String phone, String email) throws Exception {
        Response<ContactPersonSL> response = supplierService.addContactPerson(businessNumber, cpName, phone, email);
        if (response.isSuccess()) {
            ContactPersonSL data = response.getData();
            return new ContactPersonPL(data.getName(), data.getPhone(), data.getEmail());
        }
        throw new Exception(response.getErrorMessage());
    }

    public boolean removeContactPerson(String businessNumber, String phone) throws Exception {
        return extractBooleanOrThrow(supplierService.removeContactPerson(businessNumber, phone));
    }

    public AgreementPL addAgreement(String businessNumber, List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) throws Exception {
        Response<AgreementSL> response = supplierService.addAgreement(businessNumber, fixedDeliveryDays, supplierTransports);
        if (response.isSuccess()) return convertAgreementSLToPL(response.getData());
        throw new Exception(response.getErrorMessage());
    }

    public boolean removeAgreement(String businessNumber, int agreementId) throws Exception {
        return extractBooleanOrThrow(supplierService.removeAgreement(businessNumber, agreementId));
    }

    public ProductLinePL addProductLine(String businessNumber, int agreementId, int internalCatalogId, int supplierCatalogId, double price) throws Exception {
        Response<ProductLineSL> response = supplierService.addProductLine(businessNumber, agreementId, internalCatalogId, supplierCatalogId, price);
        if (response.isSuccess()) {
            ProductLineSL data = response.getData();
            return new ProductLinePL(data.getInternalCatalogId(), data.getSupplierCatalogId(), data.getAgreedPrice());
        }
        throw new Exception(response.getErrorMessage());
    }

    public boolean removeProductLine(String businessNumber, int agreementId, int internalCatalogId) throws Exception {
        return extractBooleanOrThrow(supplierService.removeProductLine(businessNumber, agreementId, internalCatalogId));
    }

    public ProductLinePL updateProductLine(String businessNumber, int agreementId, int internalCatalogId, double newPrice) throws Exception {
        Response<ProductLineSL> response = supplierService.updateProductLine(businessNumber, agreementId, internalCatalogId, newPrice);
        if (response.isSuccess()) {
            ProductLineSL data = response.getData();
            return new ProductLinePL(data.getInternalCatalogId(), data.getSupplierCatalogId(), data.getAgreedPrice());
        }
        throw new Exception(response.getErrorMessage());
    }

    public boolean addDiscount(String businessNumber, int agreementId, int internalCatalogId, int minQuantity, double discountPercentage) throws Exception {
        return extractBooleanOrThrow(supplierService.addDiscount(businessNumber, agreementId, internalCatalogId, minQuantity, discountPercentage));
    }

    public boolean removeDiscount(String businessNumber, int agreementId, int internalCatalogId, int minQuantity) throws Exception {
        return extractBooleanOrThrow(supplierService.removeDiscount(businessNumber, agreementId, internalCatalogId, minQuantity));
    }

    public boolean updateDiscount(String businessNumber, int agreementId, int internalCatalogId, int minQuantity, double newDiscountPercentage) throws Exception {
        return extractBooleanOrThrow(supplierService.updateDiscount(businessNumber, agreementId, internalCatalogId, minQuantity, newDiscountPercentage));
    }

    public boolean updateDeliveryTerms(String businessNumber, int agreementId, List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) throws Exception {
        return extractBooleanOrThrow(supplierService.updateDeliveryTerms(businessNumber, agreementId, fixedDeliveryDays, supplierTransports));
    }

    public boolean updateBankAccount(String businessNumber, String newIban) throws Exception {
        return extractBooleanOrThrow(supplierService.updateBankAccount(businessNumber, newIban));
    }

    public boolean updatePaymentTerms(String businessNumber, String newTerms) throws Exception {
        return extractBooleanOrThrow(supplierService.updatePaymentTerms(businessNumber, newTerms));
    }

    public ContactPersonPL updateContactName(String businessNumber, String phone, String newName) throws Exception {
        return extractContactOrThrow(supplierService.updateContactName(businessNumber, phone, newName));
    }

    public ContactPersonPL updateContactPhone(String businessNumber, String oldPhone, String newPhone) throws Exception {
        return extractContactOrThrow(supplierService.updateContactPhone(businessNumber, oldPhone, newPhone));
    }

    public ContactPersonPL updateContactEmail(String businessNumber, String phone, String newEmail) throws Exception {
        return extractContactOrThrow(supplierService.updateContactEmail(businessNumber, phone, newEmail));
    }

    private AgreementPL convertAgreementSLToPL(AgreementSL data) {
        DeliveryTermsPL deliveryTermsPL = new DeliveryTermsPL(data.getDeliveryTerms().getFixedDeliveryDays(), data.getDeliveryTerms().isSupplierTransports());
        List<ProductLinePL> productLinesPL = data.getProductLines().stream()
                .map(pl -> new ProductLinePL(pl.getInternalCatalogId(), pl.getSupplierCatalogId(), pl.getAgreedPrice()))
                .collect(Collectors.toList());
        Map<Integer, List<DiscountBracketPL>> discountPolicyPL = data.getDiscountPolicy().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .map(db -> new DiscountBracketPL(db.getMinQuantity(), db.getDiscountPercentage()))
                                .collect(Collectors.toList())
                ));
        return new AgreementPL(data.getAgreementId(), data.getStartDate(), deliveryTermsPL, productLinesPL, discountPolicyPL);
    }

    private ContactPersonPL extractContactOrThrow(Response<ContactPersonSL> response) throws Exception {
        if (response.isSuccess()) {
            ContactPersonSL data = response.getData();
            return new ContactPersonPL(data.getName(), data.getPhone(), data.getEmail());
        }
        throw new Exception(response.getErrorMessage());
    }

    private boolean extractBooleanOrThrow(Response<Boolean> response) throws Exception {
        if (response.isSuccess()) return response.getData();
        throw new Exception(response.getErrorMessage());
    }
}