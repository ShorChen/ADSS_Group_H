package Suppliers.Domain.Service;

import Suppliers.Domain.Business.AgreementBL;
import Suppliers.Domain.Business.ProductLineBL;
import Suppliers.Domain.Business.SupplierBL;
import Suppliers.Domain.Business.SupplierFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    private final SupplierFacade supplierFacade;

    public OrderService(SupplierFacade supplierFacade) {
        this.supplierFacade = supplierFacade;
    }

    public Response<List<String>> getAllSupplierBusinessNumbers() {
        try {
            List<String> numbers = supplierFacade.getAllSuppliers().stream()
                    .map(SupplierBL::getBusinessNumber)
                    .collect(Collectors.toList());
            return new Response<>(numbers);
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<List<PurchasableItemSL>> viewPurchasableItems(String businessNumber) {
        try {
            SupplierBL supplier = supplierFacade.getSupplier(businessNumber);
            List<PurchasableItemSL> items = new ArrayList<>();
            for (AgreementBL agreement : supplier.getAgreements()) {
                if (agreement.getDeliveryTerms().isOnDemand()) {
                    for (ProductLineBL pl : agreement.getProductLines()) {
                        double finalPrice = agreement.calculateTotal(pl.getSupplierCatalogId());
                        items.add(new PurchasableItemSL(pl.getName(), pl.getSupplierCatalogId(), pl.getBasePrice(), pl.getQuantity(), finalPrice));
                    }
                }
            }
            return new Response<>(items);
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }
}