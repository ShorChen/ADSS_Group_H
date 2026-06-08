package Suppliers.Domain.Entities;

import Suppliers.Domain.ValueObjects.OrderItemDL;
import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class OrderDL {
    private final int orderId;
    private final LocalDate orderDate;
    private final String supplierBusinessNumber;
    private final String supplierName;
    private final String address;
    private final String contactPhone;
    private final List<OrderItemDL> items;

    public OrderDL(int orderId, String supplierBusinessNumber, String supplierName, String address, String contactPhone, List<OrderItemDL> items) {
        this.orderId = orderId;
        this.orderDate = LocalDate.now();
        this.supplierBusinessNumber = supplierBusinessNumber;
        this.supplierName = supplierName;
        this.address = address;
        this.contactPhone = contactPhone;
        this.items = items;
    }

    public OrderDL(int orderId, LocalDate orderDate, String supplierBusinessNumber, String supplierName, String address, String contactPhone, List<OrderItemDL> items) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.supplierBusinessNumber = supplierBusinessNumber;
        this.supplierName = supplierName;
        this.address = address;
        this.contactPhone = contactPhone;
        this.items = items;
    }

    public int getOrderId() { return orderId; }
    public LocalDate getOrderDate() { return orderDate; }
    public String getSupplierBusinessNumber() { return supplierBusinessNumber; }
    public String getSupplierName() { return supplierName; }
    public String getAddress() { return address; }
    public String getContactPhone() { return contactPhone; }
    public List<OrderItemDL> getItems() { return items; }
}