package Suppliers.DataAccess;

import Suppliers.Domain.Entities.OrderDL;

import java.util.Map;

public interface OrderDAO {
    void addOrder(OrderDL order);

    Map<Integer, OrderDL> getAllOrders();
}