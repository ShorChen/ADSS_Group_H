package Suppliers.Presentation.Controller;

import Suppliers.Domain.Security.Role;
import Suppliers.Domain.Security.SessionManager;
import Suppliers.Presentation.DTO.*;
import Suppliers.Service.Core.SupplierService;
import Suppliers.Service.DTO.*;
import Suppliers.Service.Response;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({"UnusedReturnValue", "ClassCanBeRecord"})
public class SupplierController {
    private final SupplierService supplierService;

    SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    private void ensureSupplierManager() {
        SessionManager.getInstance().requireRole(Role.SUPPLIER_MANAGER);
    }

    public SupplierPL addSupplier(String name, String businessNumber, String address, String iban, String paymentTerms) throws Exception {
        ensureSupplierManager();
        Response<SupplierSL> response = supplierService.addSupplier(name, businessNumber, address, iban, paymentTerms);
        if (response.isSuccess()) {
            SupplierSL supplierSL = response.getData();
            List<ContactPersonPL> contacts = supplierSL.contactPersonnel().stream()
                    .map(cp -> new ContactPersonPL(cp.name(), cp.phone(), cp.email()))
                    .collect(Collectors.toList());
            List<AgreementPL> agreements = supplierSL.agreements().stream()
                    .map(this::convertAgreementSLToPL)
                    .collect(Collectors.toList());
            List<String> manufacturers = new ArrayList<>(supplierSL.manufacturers());
            return new SupplierPL(supplierSL.name(), supplierSL.businessNumber(), supplierSL.address(), contacts, agreements, manufacturers);
        }
        throw new Exception(response.getErrorMessage());
    }

    public boolean deleteSupplier(String businessNumber) throws Exception {
        ensureSupplierManager();
        return extractBooleanOrThrow(supplierService.deleteSupplier(businessNumber));
    }

    public ContactPersonPL addContactPerson(String businessNumber, String cpName, String phone, String email) throws Exception {
        ensureSupplierManager();
        Response<ContactPersonSL> response = supplierService.addContactPerson(businessNumber, cpName, phone, email);
        if (response.isSuccess()) {
            ContactPersonSL data = response.getData();
            return new ContactPersonPL(data.name(), data.phone(), data.email());
        }
        throw new Exception(response.getErrorMessage());
    }

    public boolean removeContactPerson(String businessNumber, String phone) throws Exception {
        ensureSupplierManager();
        return extractBooleanOrThrow(supplierService.removeContactPerson(businessNumber, phone));
    }

    public AgreementPL addAgreement(String businessNumber, List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) throws Exception {
        ensureSupplierManager();
        Response<AgreementSL> response = supplierService.addAgreement(businessNumber, fixedDeliveryDays, supplierTransports);
        if (response.isSuccess()) return convertAgreementSLToPL(response.getData());
        throw new Exception(response.getErrorMessage());
    }

    public boolean removeAgreement(String businessNumber, int agreementId) throws Exception {
        ensureSupplierManager();
        return extractBooleanOrThrow(supplierService.removeAgreement(businessNumber, agreementId));
    }

    public ProductLinePL addProductLine(String businessNumber, int agreementId, int supplierCatalogId, String name, double basePrice, int quantity) throws Exception {
        ensureSupplierManager();
        Response<ProductLineSL> response = supplierService.addProductLine(businessNumber, agreementId, supplierCatalogId, name, basePrice, quantity);
        if (response.isSuccess()) {
            ProductLineSL data = response.getData();
            return new ProductLinePL(data.supplierCatalogId(), data.name(), data.basePrice(), data.quantity());
        }
        throw new Exception(response.getErrorMessage());
    }

    public boolean removeProductLine(String businessNumber, int agreementId, int supplierCatalogId) throws Exception {
        ensureSupplierManager();
        return extractBooleanOrThrow(supplierService.removeProductLine(businessNumber, agreementId, supplierCatalogId));
    }

    public ProductLinePL updateProductLineBasePrice(String businessNumber, int agreementId, int supplierCatalogId, double newBasePrice) throws Exception {
        ensureSupplierManager();
        Response<ProductLineSL> response = supplierService.updateProductLineBasePrice(businessNumber, agreementId, supplierCatalogId, newBasePrice);
        if (response.isSuccess()) {
            ProductLineSL data = response.getData();
            return new ProductLinePL(data.supplierCatalogId(), data.name(), data.basePrice(), data.quantity());
        }
        throw new Exception(response.getErrorMessage());
    }

    public ProductLinePL updateProductLineQuantity(String businessNumber, int agreementId, int supplierCatalogId, int newQuantity) throws Exception {
        ensureSupplierManager();
        Response<ProductLineSL> response = supplierService.updateProductLineQuantity(businessNumber, agreementId, supplierCatalogId, newQuantity);
        if (response.isSuccess()) {
            ProductLineSL data = response.getData();
            return new ProductLinePL(data.supplierCatalogId(), data.name(), data.basePrice(), data.quantity());
        }
        throw new Exception(response.getErrorMessage());
    }

    public boolean addDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity, double discountPercentage) throws Exception {
        ensureSupplierManager();
        return extractBooleanOrThrow(supplierService.addDiscount(businessNumber, agreementId, supplierCatalogId, minQuantity, discountPercentage));
    }

    public boolean removeDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity) throws Exception {
        ensureSupplierManager();
        return extractBooleanOrThrow(supplierService.removeDiscount(businessNumber, agreementId, supplierCatalogId, minQuantity));
    }

    public boolean updateDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity, double newDiscountPercentage) throws Exception {
        ensureSupplierManager();
        return extractBooleanOrThrow(supplierService.updateDiscount(businessNumber, agreementId, supplierCatalogId, minQuantity, newDiscountPercentage));
    }

    public boolean updateFixedDeliveryDays(String businessNumber, int agreementId, List<DayOfWeek> fixedDeliveryDays) throws Exception {
        ensureSupplierManager();
        return extractBooleanOrThrow(supplierService.updateFixedDeliveryDays(businessNumber, agreementId, fixedDeliveryDays));
    }

    public boolean updateSupplierTransports(String businessNumber, int agreementId, boolean supplierTransports) throws Exception {
        ensureSupplierManager();
        return extractBooleanOrThrow(supplierService.updateSupplierTransports(businessNumber, agreementId, supplierTransports));
    }

    public boolean updateBankAccount(String businessNumber, String newIban) throws Exception {
        ensureSupplierManager();
        return extractBooleanOrThrow(supplierService.updateBankAccount(businessNumber, newIban));
    }

    public boolean updatePaymentTerms(String businessNumber, String newTerms) throws Exception {
        ensureSupplierManager();
        return extractBooleanOrThrow(supplierService.updatePaymentTerms(businessNumber, newTerms));
    }

    public boolean updateAddress(String businessNumber, String newAddress) throws Exception {
        ensureSupplierManager();
        return extractBooleanOrThrow(supplierService.updateAddress(businessNumber, newAddress));
    }

    public boolean addManufacturer(String businessNumber, String manufacturer) throws Exception {
        ensureSupplierManager();
        return extractBooleanOrThrow(supplierService.addManufacturer(businessNumber, manufacturer));
    }

    public boolean removeManufacturer(String businessNumber, String manufacturer) throws Exception {
        ensureSupplierManager();
        return extractBooleanOrThrow(supplierService.removeManufacturer(businessNumber, manufacturer));
    }

    public ContactPersonPL updateContactName(String businessNumber, String phone, String newName) throws Exception {
        ensureSupplierManager();
        return extractContactOrThrow(supplierService.updateContactName(businessNumber, phone, newName));
    }

    public ContactPersonPL updateContactPhone(String businessNumber, String oldPhone, String newPhone) throws Exception {
        ensureSupplierManager();
        return extractContactOrThrow(supplierService.updateContactPhone(businessNumber, oldPhone, newPhone));
    }

    public ContactPersonPL updateContactEmail(String businessNumber, String phone, String newEmail) throws Exception {
        ensureSupplierManager();
        return extractContactOrThrow(supplierService.updateContactEmail(businessNumber, phone, newEmail));
    }

    public List<SupplierPL> getAllSuppliers() throws Exception {
        Response<List<SupplierSL>> response = supplierService.getAllSuppliers();
        if (response.isSuccess())
            return response.getData().stream().map(sl -> {
                List<ContactPersonPL> contacts = sl.contactPersonnel().stream()
                        .map(cp -> new ContactPersonPL(cp.name(), cp.phone(), cp.email()))
                        .collect(Collectors.toList());
                List<AgreementPL> agreements = sl.agreements().stream()
                        .map(this::convertAgreementSLToPL)
                        .collect(Collectors.toList());
                List<String> manufacturers = new ArrayList<>(sl.manufacturers());
                return new SupplierPL(sl.name(), sl.businessNumber(), sl.address(), contacts, agreements, manufacturers);
            }).collect(Collectors.toList());
        throw new Exception(response.getErrorMessage());
    }

    private AgreementPL convertAgreementSLToPL(AgreementSL data) {
        DeliveryTermsPL deliveryTermsPL = new DeliveryTermsPL(data.deliveryTerms().fixedDeliveryDays(), data.deliveryTerms().supplierTransports());
        List<ProductLinePL> productLinesPL = data.productLines().stream()
                .map(pl -> new ProductLinePL(pl.supplierCatalogId(), pl.name(), pl.basePrice(), pl.quantity()))
                .collect(Collectors.toList());
        Map<Integer, List<DiscountBracketPL>> discountPolicyPL = data.discountPolicy().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .map(db -> new DiscountBracketPL(db.minQuantity(), db.discountPercentage()))
                                .collect(Collectors.toList())
                ));
        return new AgreementPL(data.agreementId(), data.startDate(), deliveryTermsPL, productLinesPL, discountPolicyPL);
    }

    private ContactPersonPL extractContactOrThrow(Response<ContactPersonSL> response) throws Exception {
        if (response.isSuccess()) {
            ContactPersonSL data = response.getData();
            return new ContactPersonPL(data.name(), data.phone(), data.email());
        }
        throw new Exception(response.getErrorMessage());
    }

    private boolean extractBooleanOrThrow(Response<Boolean> response) throws Exception {
        if (response.isSuccess()) return response.getData();
        throw new Exception(response.getErrorMessage());
    }
}