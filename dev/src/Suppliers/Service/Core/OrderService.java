package Suppliers.Service.Core;

import Suppliers.Domain.Entities.AgreementDL;
import Suppliers.Domain.Entities.OrderDL;
import Suppliers.Domain.ValueObjects.OrderItemDL;
import Suppliers.Domain.Entities.ProductLineDL;
import Suppliers.Domain.Entities.SupplierDL;
import Suppliers.Domain.Facades.OrderFacade;
import Suppliers.Domain.Facades.SupplierFacade;
import Core.Service.Response;
import Suppliers.Service.DTO.SupplierSL;
import Suppliers.Service.DTO.OrderSL;
import Suppliers.Service.DTO.OrderItemSL;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ClassCanBeRecord")
public class OrderService {
    private final SupplierFacade supplierFacade;
    private final OrderFacade orderFacade;

    public OrderService(SupplierFacade supplierFacade, OrderFacade orderFacade) {
        this.supplierFacade = supplierFacade;
        this.orderFacade = orderFacade;
    }

    public Response<List<SupplierSL>> getAllSuppliers() {
        try {
            List<SupplierSL> allSuppliers = new ArrayList<>();
            for (SupplierDL dl : supplierFacade.getAllSuppliers())
                allSuppliers.add(new SupplierSL(dl));
            return new Response<>(allSuppliers);
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Integer> placeOrder(String businessNumber, List<OrderItemSL> items) {
        try {
            SupplierDL supplier = supplierFacade.getSupplierOrThrow(businessNumber);
            String address = supplier.getAddress();
            String phones = supplier.getContactPersonnel().isEmpty() ? "N/A" :
                    supplier.getContactPersonnel().stream().map(cp -> cp.getName() + " (" + cp.getPhone() + ")").collect(Collectors.joining(", "));
            List<OrderItemDL> itemsDL = items.stream().map(sl -> new OrderItemDL(
                    sl.catalogId(),
                    sl.productName(),
                    sl.quantity(),
                    sl.priceBeforeDiscount(),
                    sl.priceBeforeDiscount() - sl.finalPrice(),
                    sl.finalPrice()
            )).collect(Collectors.toList());
            OrderDL order = orderFacade.createOrder(businessNumber, supplier.getName(), address, phones, itemsDL);
            return new Response<>(order.getOrderId());
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Integer> executeAutomaticOrders() {
        try {
            DayOfWeek tomorrow = LocalDate.now().plusDays(1).getDayOfWeek();
            int count = 0;
            for (SupplierDL supplier : supplierFacade.getAllSuppliers())
                for (AgreementDL agreement : supplier.getAgreements())
                    if (!agreement.getDeliveryTerms().isOnDemand() && agreement.getDeliveryTerms().getFixedDeliveryDays().contains(tomorrow)) {
                        List<OrderItemDL> items = new ArrayList<>();
                        for (ProductLineDL pl : agreement.getProductLines()) {
                            double discountPct = agreement.getDiscountPolicy().calculateDiscount(pl.getSupplierCatalogId(), pl.getQuantity());
                            double listPrice = pl.getBasePrice() * pl.getQuantity();
                            double finalPrice = listPrice * (1.0 - (discountPct / 100.0));
                            items.add(new OrderItemDL(pl.getSupplierCatalogId(), pl.getName(), pl.getQuantity(), listPrice, listPrice - finalPrice, finalPrice));
                        }
                        if (!items.isEmpty()) {
                            String phones = supplier.getContactPersonnel().isEmpty() ? "N/A" : supplier.getContactPersonnel().stream().map(cp -> cp.getName() + " (" + cp.getPhone() + ")").collect(Collectors.joining(", "));
                            orderFacade.createOrder(supplier.getBusinessNumber(), supplier.getName(), supplier.getAddress(), phones, items);
                            count++;
                        }
                    }
            return new Response<>(count);
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<List<OrderSL>> getOrderHistory() {
        try {
            List<OrderDL> historyDL = orderFacade.getOrderHistory();
            List<OrderSL> historySL = historyDL.stream().map(dl -> new OrderSL(
                    dl.getOrderId(),
                    dl.getSupplierBusinessNumber(),
                    dl.getSupplierName(),
                    dl.getAddress(),
                    dl.getContactPhone(),
                    dl.getOrderDate(),
                    dl.getItems().stream().map(itemDL -> new OrderItemSL(
                            itemDL.productName(),
                            dl.getSupplierBusinessNumber(),
                            dl.getSupplierName(),
                            itemDL.catalogId(),
                            itemDL.quantity(),
                            itemDL.listPrice(),
                            itemDL.finalPrice()
                    )).collect(Collectors.toList())
            )).collect(Collectors.toList());
            return new Response<>(historySL);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }
}