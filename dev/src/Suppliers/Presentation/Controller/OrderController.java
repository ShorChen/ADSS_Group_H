package Suppliers.Presentation.Controller;

import Suppliers.Domain.Security.Role;
import Suppliers.Domain.Security.SessionManager;
import Suppliers.Presentation.DTO.*;
import Suppliers.Service.Core.OrderService;
import Suppliers.Service.DTO.AgreementSL;
import Suppliers.Service.Response;
import Suppliers.Service.DTO.SupplierSL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("ClassCanBeRecord")
public class OrderController {
    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public List<SupplierPL> getOnDemandSuppliers() throws Exception {
        SessionManager.getInstance().requireRole(Role.ORDER_MANAGER);
        Response<List<SupplierSL>> response = orderService.getOnDemandSuppliers();
        if (response.isSuccess()) return response.getData().stream().map(sl -> {
            List<ContactPersonPL> contacts = sl.contactPersonnel().stream().map(cp -> new ContactPersonPL(cp.name(), cp.phone(), cp.email())).collect(Collectors.toList());
            List<AgreementPL> agreements = sl.agreements().stream().map(this::convertAgreementSLToPL).collect(Collectors.toList());
            List<String> manufacturers = new ArrayList<>(sl.manufacturers());
            return new SupplierPL(sl.name(), sl.businessNumber(), sl.address(), contacts, agreements, manufacturers);
        }).collect(Collectors.toList());
        throw new Exception(response.getErrorMessage());
    }

    public Map<String, Integer> placeOnDemandOrders(List<OrderItemPL> currentOrder) throws Exception {
        SessionManager.getInstance().requireRole(Role.ORDER_MANAGER);
        Map<String, List<OrderItemPL>> grouped = currentOrder.stream().collect(Collectors.groupingBy(OrderItemPL::supplierBusinessNumber));
        Map<String, Integer> generatedOrderIds = new HashMap<>();
        for (Map.Entry<String, List<OrderItemPL>> entry : grouped.entrySet()) {
            String businessNumber = entry.getKey();
            String supplierName = entry.getValue().getFirst().supplierName();
            List<Integer> catalogIds = new ArrayList<>();
            List<String> productNames = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();
            List<Double> listPrices = new ArrayList<>();
            List<Double> discounts = new ArrayList<>();
            List<Double> finalPrices = new ArrayList<>();
            for (OrderItemPL item : entry.getValue()) {
                catalogIds.add(item.catalogId());
                productNames.add(item.productName());
                quantities.add(item.quantity());
                listPrices.add(item.priceBeforeDiscount());
                discounts.add(item.priceBeforeDiscount() - item.totalPrice());
                finalPrices.add(item.totalPrice());
            }
            Response<Integer> response = orderService.placeOrder(
                    businessNumber, catalogIds, productNames, quantities, listPrices, discounts, finalPrices
            );
            if (!response.isSuccess()) throw new Exception(response.getErrorMessage());
            generatedOrderIds.put(supplierName, response.getData());
        }
        return generatedOrderIds;
    }

    public int executeAutomaticOrders() throws Exception {
        SessionManager.getInstance().requireRole(Role.ORDER_MANAGER);
        Response<Integer> response = orderService.executeAutomaticOrders();
        if (response.isSuccess()) return response.getData();
        throw new Exception(response.getErrorMessage());
    }

    private AgreementPL convertAgreementSLToPL(AgreementSL data) {
        DeliveryTermsPL deliveryTermsPL = new DeliveryTermsPL(data.deliveryTerms().fixedDeliveryDays(), data.deliveryTerms().supplierTransports());
        List<ProductLinePL> productLinesPL = data.productLines().stream().map(pl -> new ProductLinePL(pl.supplierCatalogId(), pl.name(), pl.basePrice(), pl.quantity())).collect(Collectors.toList());
        Map<Integer, List<DiscountBracketPL>> discountPolicyPL = data.discountPolicy().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(db -> new DiscountBracketPL(db.minQuantity(), db.discountPercentage())).collect(Collectors.toList())));
        return new AgreementPL(data.agreementId(), data.startDate(), deliveryTermsPL, productLinesPL, discountPolicyPL);
    }
}