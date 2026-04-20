package Suppliers.Domain.Entities;

import Suppliers.Domain.ValueObjects.OrderItemDL;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class OrderDL implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static int idCounter = 0;
    private final int orderId;
    private final LocalDate orderDate;
    private final String supplierBusinessNumber;
    private final String supplierName;
    private final String address;
    private final String contactPhone;
    private final List<OrderItemDL> items;

    public OrderDL(String supplierBusinessNumber, String supplierName, String address, String contactPhone, List<OrderItemDL> items) {
        this.orderId = ++idCounter;
        this.orderDate = LocalDate.now();
        this.supplierBusinessNumber = supplierBusinessNumber;
        this.supplierName = supplierName;
        this.address = address;
        this.contactPhone = contactPhone;
        this.items = items;
    }

    public static void updateIdCounter(int maxId) {
        if (maxId > idCounter) idCounter = maxId;
    }

    public int getOrderId() {
        return orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getSupplierBusinessNumber() {
        return supplierBusinessNumber;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getAddress() {
        return address;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public List<OrderItemDL> getItems() {
        return items;
    }
}