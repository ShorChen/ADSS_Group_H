package Suppliers.Presentation.Controller;

import Suppliers.Domain.Security.Role;
import Suppliers.Domain.Security.SessionManager;
import Suppliers.Presentation.DTO.*;
import Suppliers.Service.Core.OrderService;
import Suppliers.Service.DTO.AgreementSL;
import Suppliers.Service.DTO.OrderSL;
import Suppliers.Service.DTO.OrderItemSL;
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

    // RENAMED: Accurately reflects fetching all suppliers
    public List<SupplierPL> getAllSuppliers() throws Exception {
        SessionManager.getInstance().requireRole(Role.ORDER_MANAGER);
        Response<List<SupplierSL>> response = orderService.getAllSuppliers();
        if (response.isSuccess()) return response.getData().stream().map(sl -> {
            List<ContactPersonPL> contacts = sl.contactPersonnel().stream().map(cp -> new ContactPersonPL(cp.name(), cp.phone(), cp.email())).collect(Collectors.toList());
            List<AgreementPL> agreements = sl.agreements().stream().map(this::convertAgreementSLToPL).collect(Collectors.toList());
            List<String> manufacturers = new ArrayList<>(sl.manufacturers());
            return new SupplierPL(sl.name(), sl.businessNumber(), sl.address(), contacts, agreements, manufacturers);
        }).collect(Collectors.toList());
        throw new Exception(response.getErrorMessage());
    }

    // RENAMED & REFACTORED: Passes a mapped list of DTOs instead of 6 parallel arrays
    public Map<String, Integer> placeOrders(List<OrderItemPL> currentOrder) throws Exception {
        SessionManager.getInstance().requireRole(Role.ORDER_MANAGER);

        // Group the big cart by supplier
        Map<String, List<OrderItemPL>> grouped = currentOrder.stream().collect(Collectors.groupingBy(OrderItemPL::supplierBusinessNumber));
        Map<String, Integer> generatedOrderIds = new HashMap<>();

        for (Map.Entry<String, List<OrderItemPL>> entry : grouped.entrySet()) {
            String businessNumber = entry.getKey();
            String supplierName = entry.getValue().get(0).supplierName();

            // Map Presentation Layer items to Service Layer items
            List<OrderItemSL> slItems = entry.getValue().stream().map(pl -> new OrderItemSL(
                    pl.productName(), pl.supplierBusinessNumber(), pl.supplierName(),
                    pl.catalogId(), pl.quantity(), pl.priceBeforeDiscount(), pl.finalPrice() // Assumes your record uses 'finalPrice' or 'totalPrice'
            )).collect(Collectors.toList());

            // Send the clean list to the service layer
            Response<Integer> response = orderService.placeOrder(businessNumber, slItems);

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

    // FIX: Safely reads Service Objects and converts them to Presentation Objects for the GUI
    public List<OrderPL> getOrderHistory() throws Exception {
        SessionManager.getInstance().requireRole(Role.ORDER_MANAGER);
        Response<List<OrderSL>> response = orderService.getOrderHistory();
        if (!response.isSuccess()) throw new Exception(response.getErrorMessage());

        return response.getData().stream().map(sl -> new OrderPL(
                sl.orderId(),
                sl.supplierBusinessNumber(),
                sl.supplierName(),
                sl.address(),
                sl.contactPhone(),
                sl.orderDate(),
                sl.items().stream().map(itemSL -> new OrderItemPL(
                        itemSL.productName(), itemSL.supplierBusinessNumber(), itemSL.supplierName(),
                        itemSL.catalogId(), itemSL.quantity(), itemSL.priceBeforeDiscount(), itemSL.finalPrice()
                )).collect(Collectors.toList())
        )).collect(Collectors.toList());
    }
}