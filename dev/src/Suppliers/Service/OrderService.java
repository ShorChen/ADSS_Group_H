package Suppliers.Service;

import Suppliers.Domain.AgreementDL;
import Suppliers.Domain.ProductLineDL;
import Suppliers.Domain.SupplierDL;
import Suppliers.Domain.SupplierFacade;

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
                    .map(SupplierDL::getBusinessNumber)
                    .collect(Collectors.toList());
            return new Response<>(numbers);
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<List<PurchasableItemSL>> viewPurchasableItems(String businessNumber) {
        try {
            SupplierDL supplier = supplierFacade.getSupplier(businessNumber);
            List<PurchasableItemSL> items = new ArrayList<>();
            for (AgreementDL agreement : supplier.getAgreements())
                if (agreement.getDeliveryTerms().isOnDemand())
                    for (ProductLineDL pl : agreement.getProductLines()) {
                        double finalPrice = agreement.calculateTotal(pl.getSupplierCatalogId());
                        items.add(new PurchasableItemSL(pl, finalPrice));
                    }
            return new Response<>(items);
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }
}