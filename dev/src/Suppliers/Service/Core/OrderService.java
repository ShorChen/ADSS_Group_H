package Suppliers.Service.Core;

import Suppliers.Domain.Entities.AgreementDL;
import Suppliers.Domain.ValueObjects.OrderItemDL;
import Suppliers.Domain.Entities.ProductLineDL;
import Suppliers.Domain.Entities.SupplierDL;
import Suppliers.Domain.Facades.OrderFacade;
import Suppliers.Domain.Facades.SupplierFacade;
import Suppliers.Service.Response;
import Suppliers.Service.DTO.SupplierSL;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    private final SupplierFacade supplierFacade;
    private final OrderFacade orderFacade;

    public OrderService(SupplierFacade supplierFacade, OrderFacade orderFacade) {
        this.supplierFacade = supplierFacade;
        this.orderFacade = orderFacade;
    }

    /*
        public Response<List<SupplierSL>> getOnDemandSuppliers() {
        try {
            List<SupplierSL> onDemandSuppliers = new ArrayList<>();
            for (SupplierDL dl : supplierFacade.getOnDemandSuppliers()) {
                List<AgreementSL> onDemandAgreements = new ArrayList<>();
                for (AgreementDL agr : dl.getOnDemandAgreements()) onDemandAgreements.add(new AgreementSL(agr));
                onDemandSuppliers.add(new SupplierSL(dl, onDemandAgreements));
            }
            return new Response<>(onDemandSuppliers);
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }
     */

    // The trivial solution is the one above, as written in the description of the module. But Max said that for
    // suppliers with fixed days agreements, we should allow to make an order in other days too, if it is urgent
    public Response<List<SupplierSL>> getOnDemandSuppliers() {
        try {
            List<SupplierSL> onDemandSuppliers = new ArrayList<>();
            for (SupplierDL dl : supplierFacade.getAllSuppliers())
                onDemandSuppliers.add(new SupplierSL(dl));
            return new Response<>(onDemandSuppliers);
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> placeOrder(String businessNumber, List<Integer> catalogIds, List<String> productNames,
                                        List<Integer> quantities, List<Double> listPrices, List<Double> discounts, List<Double> finalPrices) {
        try {
            SupplierDL supplier = supplierFacade.getSupplier(businessNumber);
            String address = supplier.getAddress();
            String phones = supplier.getContactPersonnel().isEmpty() ? "N/A" :
                    supplier.getContactPersonnel().stream().map(cp -> cp.getName() + " (" + cp.getPhone() + ")").collect(Collectors.joining(", "));

            List<OrderItemDL> itemsDL = new ArrayList<>();
            for (int i = 0; i < catalogIds.size(); i++) {
                itemsDL.add(new OrderItemDL(catalogIds.get(i), productNames.get(i), quantities.get(i), listPrices.get(i), discounts.get(i), finalPrices.get(i)));
            }
            orderFacade.createOrder(businessNumber, supplier.getName(), address, phones, itemsDL);
            return new Response<>(true);
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Integer> executeAutomaticOrders() {
        try {
            DayOfWeek today = LocalDate.now().getDayOfWeek();
            int count = 0;
            for (SupplierDL supplier : supplierFacade.getAllSuppliers()) {
                for (AgreementDL agreement : supplier.getAgreements()) {
                    if (!agreement.getDeliveryTerms().isOnDemand() && agreement.getDeliveryTerms().getFixedDeliveryDays().contains(today)) {
                        List<OrderItemDL> items = new ArrayList<>();
                        for (ProductLineDL pl : agreement.getProductLines()) {
                            double discountPct = agreement.getDiscountPolicy().calculateDiscount(pl.getSupplierCatalogId(), pl.getQuantity());
                            double listPrice = pl.getBasePrice() * pl.getQuantity();
                            double finalPrice = listPrice * (1.0 - (discountPct / 100.0));
                            items.add(new OrderItemDL(pl.getSupplierCatalogId(), pl.getName(), pl.getQuantity(), listPrice, listPrice - finalPrice, finalPrice));
                        }
                        if (!items.isEmpty()) {
                            String phones = supplier.getContactPersonnel().isEmpty() ? "N/A" :
                                    supplier.getContactPersonnel().stream().map(cp -> cp.getName() + " (" + cp.getPhone() + ")").collect(Collectors.joining(", "));
                            orderFacade.createOrder(supplier.getBusinessNumber(), supplier.getName(), supplier.getAddress(), phones, items);
                            count++;
                        }
                    }
                }
            }
            return new Response<>(count);
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }
}