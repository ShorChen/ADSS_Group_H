package Suppliers.Presentation.Controller;

import Suppliers.Domain.Security.Role;
import Suppliers.Domain.Security.SessionManager;
import Suppliers.Presentation.DTO.*;
import Suppliers.Service.Core.OrderService;
import Suppliers.Service.DTO.AgreementSL;
import Suppliers.Service.Response;
import Suppliers.Service.DTO.SupplierSL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderController {
    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public List<SupplierPL> getOnDemandSuppliers() throws Exception {
        SessionManager.getInstance().requireRole(Role.ORDER_MANAGER);
        Response<List<SupplierSL>> response = orderService.getOnDemandSuppliers();
        if (response.isSuccess()) return response.getData().stream().map(sl -> {
            List<ContactPersonPL> contacts = sl.getContactPersonnel().stream().map(cp -> new ContactPersonPL(cp.getName(), cp.getPhone(), cp.getEmail())).collect(Collectors.toList());
            List<AgreementPL> agreements = sl.getAgreements().stream().map(this::convertAgreementSLToPL).collect(Collectors.toList());
            List<String> manufacturers = new ArrayList<>(sl.getManufacturers());
            return new SupplierPL(sl.getName(), sl.getBusinessNumber(), sl.getAddress(), contacts, agreements, manufacturers);
        }).collect(Collectors.toList());
        throw new Exception(response.getErrorMessage());
    }

    public boolean placeOnDemandOrders(List<OrderItemPL> currentOrder) throws Exception {
        SessionManager.getInstance().requireRole(Role.ORDER_MANAGER);
        Map<String, List<OrderItemPL>> grouped = currentOrder.stream().collect(Collectors.groupingBy(OrderItemPL::getSupplierBusinessNumber));

        for (Map.Entry<String, List<OrderItemPL>> entry : grouped.entrySet()) {
            String businessNumber = entry.getKey();

            List<Integer> catalogIds = new ArrayList<>();
            List<String> productNames = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();
            List<Double> listPrices = new ArrayList<>();
            List<Double> discounts = new ArrayList<>();
            List<Double> finalPrices = new ArrayList<>();

            for (OrderItemPL item : entry.getValue()) {
                catalogIds.add(item.getCatalogId());
                productNames.add(item.getProductName());
                quantities.add(item.getQuantity());
                listPrices.add(item.getPriceBeforeDiscount());
                discounts.add(item.getPriceBeforeDiscount() - item.getTotalPrice());
                finalPrices.add(item.getTotalPrice());
            }

            Response<Boolean> response = orderService.placeOrder(
                    businessNumber, catalogIds, productNames, quantities, listPrices, discounts, finalPrices
            );

            if (!response.isSuccess()) throw new Exception(response.getErrorMessage());
        }
        return true;
    }

    public int executeAutomaticOrders() throws Exception {
        SessionManager.getInstance().requireRole(Role.ORDER_MANAGER);
        Response<Integer> response = orderService.executeAutomaticOrders();
        if (response.isSuccess()) return response.getData();
        throw new Exception(response.getErrorMessage());
    }

    private AgreementPL convertAgreementSLToPL(AgreementSL data) {
        DeliveryTermsPL deliveryTermsPL = new DeliveryTermsPL(data.getDeliveryTerms().getFixedDeliveryDays(), data.getDeliveryTerms().isSupplierTransports());
        List<ProductLinePL> productLinesPL = data.getProductLines().stream().map(pl -> new ProductLinePL(pl.getSupplierCatalogId(), pl.getName(), pl.getBasePrice(), pl.getQuantity())).collect(Collectors.toList());
        Map<Integer, List<DiscountBracketPL>> discountPolicyPL = data.getDiscountPolicy().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(db -> new DiscountBracketPL(db.getMinQuantity(), db.getDiscountPercentage())).collect(Collectors.toList())));
        return new AgreementPL(data.getAgreementId(), data.getStartDate(), deliveryTermsPL, productLinesPL, discountPolicyPL);
    }
}