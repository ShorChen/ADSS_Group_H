package Suppliers.Service.Core;

import Suppliers.Domain.Entities.SupplierDL;
import Suppliers.Domain.Facades.SupplierFacade;
import Suppliers.Service.DTO.*;
import Core.Service.Response;

import java.time.DayOfWeek;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class SupplierService {
    private final SupplierFacade supplierFacade;

    public SupplierService(SupplierFacade supplierFacade) {
        this.supplierFacade = supplierFacade;
    }

    public Response<SupplierSL> addSupplier(String name, String businessNumber, String address, String iban, String paymentTerms) {
        try {
            return new Response<>(new SupplierSL(supplierFacade.addSupplier(name, businessNumber, address, iban, paymentTerms)));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> deleteSupplier(String businessNumber) {
        try {
            return new Response<>(supplierFacade.deleteSupplier(businessNumber));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<ContactPersonSL> addContactPerson(String businessNumber, String cpName, String phone, String email) {
        try {
            return new Response<>(new ContactPersonSL(supplierFacade.addContactPerson(businessNumber, cpName, phone, email)));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> removeContactPerson(String businessNumber, String phone) {
        try {
            return new Response<>(supplierFacade.removeContactPerson(businessNumber, phone));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<AgreementSL> addAgreement(String businessNumber, List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        try {
            return new Response<>(new AgreementSL(supplierFacade.addAgreement(businessNumber, fixedDeliveryDays, supplierTransports)));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> removeAgreement(String businessNumber, int agreementId) {
        try {
            return new Response<>(supplierFacade.removeAgreement(businessNumber, agreementId));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<ProductLineSL> addProductLine(String businessNumber, int agreementId, int supplierCatalogId, String name, double basePrice, int quantity) {
        try {
            return new Response<>(new ProductLineSL(supplierFacade.addProductLine(businessNumber, agreementId, supplierCatalogId, name, basePrice, quantity)));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> removeProductLine(String businessNumber, int agreementId, int supplierCatalogId) {
        try {
            return new Response<>(supplierFacade.removeProductLine(businessNumber, agreementId, supplierCatalogId));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<ProductLineSL> updateProductLineBasePrice(String businessNumber, int agreementId, int supplierCatalogId, double newBasePrice) {
        try {
            return new Response<>(new ProductLineSL(supplierFacade.updateProductLineBasePrice(businessNumber, agreementId, supplierCatalogId, newBasePrice)));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<ProductLineSL> updateProductLineQuantity(String businessNumber, int agreementId, int supplierCatalogId, int newQuantity) {
        try {
            return new Response<>(new ProductLineSL(supplierFacade.updateProductLineQuantity(businessNumber, agreementId, supplierCatalogId, newQuantity)));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> addDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity, double discountPercentage) {
        try {
            return new Response<>(supplierFacade.addDiscount(businessNumber, agreementId, supplierCatalogId, minQuantity, discountPercentage));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> removeDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity) {
        try {
            return new Response<>(supplierFacade.removeDiscount(businessNumber, agreementId, supplierCatalogId, minQuantity));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> updateDiscount(String businessNumber, int agreementId, int supplierCatalogId, int minQuantity, double newDiscountPercentage) {
        try {
            return new Response<>(supplierFacade.updateDiscount(businessNumber, agreementId, supplierCatalogId, minQuantity, newDiscountPercentage));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> updateFixedDeliveryDays(String businessNumber, int agreementId, List<DayOfWeek> fixedDeliveryDays) {
        try {
            return new Response<>(supplierFacade.updateFixedDeliveryDays(businessNumber, agreementId, fixedDeliveryDays));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> updateSupplierTransports(String businessNumber, int agreementId, boolean supplierTransports) {
        try {
            return new Response<>(supplierFacade.updateSupplierTransports(businessNumber, agreementId, supplierTransports));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> updateBankAccount(String businessNumber, String newIban) {
        try {
            return new Response<>(supplierFacade.updateBankAccount(businessNumber, newIban));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> updatePaymentTerms(String businessNumber, String newTerms) {
        try {
            return new Response<>(supplierFacade.updatePaymentTerms(businessNumber, newTerms));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> updateAddress(String businessNumber, String newAddress) {
        try {
            return new Response<>(supplierFacade.updateAddress(businessNumber, newAddress));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> addManufacturer(String businessNumber, String manufacturer) {
        try {
            return new Response<>(supplierFacade.addManufacturer(businessNumber, manufacturer));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> removeManufacturer(String businessNumber, String manufacturer) {
        try {
            return new Response<>(supplierFacade.removeManufacturer(businessNumber, manufacturer));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<ContactPersonSL> updateContactName(String businessNumber, String phone, String newName) {
        try {
            return new Response<>(new ContactPersonSL(supplierFacade.updateContactName(businessNumber, phone, newName)));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<ContactPersonSL> updateContactPhone(String businessNumber, String oldPhone, String newPhone) {
        try {
            return new Response<>(new ContactPersonSL(supplierFacade.updateContactPhone(businessNumber, oldPhone, newPhone)));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<ContactPersonSL> updateContactEmail(String businessNumber, String phone, String newEmail) {
        try {
            return new Response<>(new ContactPersonSL(supplierFacade.updateContactEmail(businessNumber, phone, newEmail)));
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<List<SupplierSL>> getAllSuppliers() {
        try {
            List<SupplierDL> suppliersDL = supplierFacade.getAllSuppliers();
            List<SupplierSL> suppliersSL = suppliersDL.stream()
                    .map(SupplierSL::new)
                    .collect(java.util.stream.Collectors.toList());
            return new Response<>(suppliersSL);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }
}