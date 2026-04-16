package Suppliers.Presentation.Controller;

import Suppliers.Domain.Role;
import Suppliers.Domain.SessionManager;
import Suppliers.Service.OrderService;
import Suppliers.Service.PurchasableItemSL;
import Suppliers.Service.Response;
import Suppliers.Presentation.PurchasableItemPL;

import java.util.List;
import java.util.stream.Collectors;

public class OrderController {
    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public List<String> getAllSupplierBusinessNumbers() throws Exception {
        SessionManager.getInstance().requireRole(Role.ORDER_MANAGER);
        Response<List<String>> response = orderService.getAllSupplierBusinessNumbers();
        if (response.isSuccess())
            return response.getData();
        throw new Exception(response.getErrorMessage());
    }

    public List<PurchasableItemPL> viewPurchasableItems(String businessNumber) throws Exception {
        SessionManager.getInstance().requireRole(Role.ORDER_MANAGER);
        Response<List<PurchasableItemSL>> response = orderService.viewPurchasableItems(businessNumber);
        if (response.isSuccess())
            return response.getData().stream()
                    .map(sl -> new PurchasableItemPL(sl.getProductName(), sl.getSupplierCatalogId(), sl.getBasePrice(), sl.getQuantity(), sl.getFinalPrice()))
                    .collect(Collectors.toList());
        throw new Exception(response.getErrorMessage());
    }
}