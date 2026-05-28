package Suppliers.Domain.Facades;

import Suppliers.DataAccess.OrderDAO;
import Suppliers.Domain.Entities.OrderDL;
import Suppliers.Domain.ValueObjects.OrderItemDL;

import java.util.List;
import java.util.Map;

public class OrderFacade {
    private final Map<Integer, OrderDL> orders;
    private final OrderDAO orderDAO;

    public OrderFacade(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
        orders = orderDAO.getAllOrders();
    }

    public OrderDL createOrder(String businessNumber, String supplierName, String address, String phone, List<OrderItemDL> items) {
        OrderDL order = orderDAO.createAndSaveOrder(businessNumber, supplierName, address, phone, items);
        orders.put(order.getOrderId(), order);
        return order;
    }

    public List<OrderDL> getOrderHistory() {
        return new java.util.ArrayList<>(orderDAO.getAllOrders().values());
    }
}