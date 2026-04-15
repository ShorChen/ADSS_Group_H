package Suppliers.Domain.Service;

import Suppliers.Domain.Business.SupplierFacade;
import java.time.DayOfWeek;
import java.util.List;

public class SupplierService {
    private final SupplierFacade supplierFacade;

    public SupplierService(SupplierFacade supplierFacade) {
        this.supplierFacade = supplierFacade;
    }

    public Response<SupplierSL> addSupplier(String businessNumber, String iban, String paymentTerms) {
        try { return new Response<>(new SupplierSL(supplierFacade.addSupplier(businessNumber, iban, paymentTerms))); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<Boolean> deleteSupplier(String businessNumber) {
        try { return new Response<>(supplierFacade.deleteSupplier(businessNumber)); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<ContactPersonSL> addContactPerson(String businessNumber, String cpName, String phone, String email) {
        try { return new Response<>(new ContactPersonSL(supplierFacade.addContactPerson(businessNumber, cpName, phone, email))); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<Boolean> removeContactPerson(String businessNumber, String phone) {
        try { return new Response<>(supplierFacade.removeContactPerson(businessNumber, phone)); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<AgreementSL> addAgreement(String businessNumber, List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        try { return new Response<>(new AgreementSL(supplierFacade.addAgreement(businessNumber, fixedDeliveryDays, supplierTransports))); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<Boolean> removeAgreement(String businessNumber, int agreementId) {
        try { return new Response<>(supplierFacade.removeAgreement(businessNumber, agreementId)); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<ProductLineSL> addProductLine(String businessNumber, int agreementId, int internalCatalogId, int supplierCatalogId, double price) {
        try { return new Response<>(new ProductLineSL(supplierFacade.addProductLine(businessNumber, agreementId, internalCatalogId, supplierCatalogId, price))); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<Boolean> removeProductLine(String businessNumber, int agreementId, int internalCatalogId) {
        try { return new Response<>(supplierFacade.removeProductLine(businessNumber, agreementId, internalCatalogId)); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<ProductLineSL> updateProductLine(String businessNumber, int agreementId, int internalCatalogId, double newPrice) {
        try { return new Response<>(new ProductLineSL(supplierFacade.updateProductLine(businessNumber, agreementId, internalCatalogId, newPrice))); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<Boolean> addDiscount(String businessNumber, int agreementId, int internalCatalogId, int minQuantity, double discountPercentage) {
        try { return new Response<>(supplierFacade.addDiscount(businessNumber, agreementId, internalCatalogId, minQuantity, discountPercentage)); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<Boolean> removeDiscount(String businessNumber, int agreementId, int internalCatalogId, int minQuantity) {
        try { return new Response<>(supplierFacade.removeDiscount(businessNumber, agreementId, internalCatalogId, minQuantity)); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<Boolean> updateDiscount(String businessNumber, int agreementId, int internalCatalogId, int minQuantity, double newDiscountPercentage) {
        try { return new Response<>(supplierFacade.updateDiscount(businessNumber, agreementId, internalCatalogId, minQuantity, newDiscountPercentage)); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<Boolean> updateDeliveryTerms(String businessNumber, int agreementId, List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        try { return new Response<>(supplierFacade.updateDeliveryTerms(businessNumber, agreementId, fixedDeliveryDays, supplierTransports)); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<Boolean> updateBankAccount(String businessNumber, String newIban) {
        try { return new Response<>(supplierFacade.updateBankAccount(businessNumber, newIban)); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<Boolean> updatePaymentTerms(String businessNumber, String newTerms) {
        try { return new Response<>(supplierFacade.updatePaymentTerms(businessNumber, newTerms)); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<ContactPersonSL> updateContactName(String businessNumber, String phone, String newName) {
        try { return new Response<>(new ContactPersonSL(supplierFacade.updateContactName(businessNumber, phone, newName))); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<ContactPersonSL> updateContactPhone(String businessNumber, String oldPhone, String newPhone) {
        try { return new Response<>(new ContactPersonSL(supplierFacade.updateContactPhone(businessNumber, oldPhone, newPhone))); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }

    public Response<ContactPersonSL> updateContactEmail(String businessNumber, String phone, String newEmail) {
        try { return new Response<>(new ContactPersonSL(supplierFacade.updateContactEmail(businessNumber, phone, newEmail))); }
        catch (Exception ex) { return new Response<>(ex.getMessage()); }
    }
}