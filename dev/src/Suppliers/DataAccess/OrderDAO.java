package Suppliers.DataAccess;

import Suppliers.Domain.Entities.OrderDL;
import Suppliers.Domain.ValueObjects.OrderItemDL;
import java.util.List;
import java.util.Map;

public interface OrderDAO {
    OrderDL createAndSaveOrder(String businessNumber, String supplierName, String address, String phone, List<OrderItemDL> items);
    OrderDL getOrder(int orderId);
    Map<Integer, OrderDL> getAllOrders();
}