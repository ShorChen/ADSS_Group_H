package Suppliers.DataAccess.SqlImpl;

import Core.DataAccess.DatabaseManager;
import Suppliers.DataAccess.OrderDAO;
import Suppliers.Domain.Entities.OrderDL;
import Suppliers.Domain.ValueObjects.OrderItemDL;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlOrderDAO implements OrderDAO {

    @Override
    public OrderDL createAndSaveOrder(String businessNumber, String supplierName, String address, String phone, List<OrderItemDL> items) {
        String insertOrder = "INSERT INTO Orders(businessNumber, supplierName, address, contactPhone, orderDate) VALUES(?,?,?,?,?)";
        String insertItem = "INSERT INTO OrderItems(orderId, catalogId, productName, quantity, listPrice, discount, finalPrice) VALUES(?,?,?,?,?,?,?)";
        int generatedId;
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setString(1, businessNumber);
                orderStmt.setString(2, supplierName);
                orderStmt.setString(3, address);
                orderStmt.setString(4, phone);
                orderStmt.setString(5, LocalDate.now().toString());
                orderStmt.executeUpdate();
                try (ResultSet rs = orderStmt.getGeneratedKeys()) {
                    if (rs.next()) generatedId = rs.getInt(1);
                    else throw new SQLException("Failed to retrieve generated Order ID.");
                }
            }
            try (PreparedStatement itemStmt = conn.prepareStatement(insertItem)) {
                for (OrderItemDL item : items) {
                    itemStmt.setInt(1, generatedId);
                    itemStmt.setInt(2, item.catalogId());
                    itemStmt.setString(3, item.productName());
                    itemStmt.setInt(4, item.quantity());
                    itemStmt.setDouble(5, item.listPrice());
                    itemStmt.setDouble(6, item.discount());
                    itemStmt.setDouble(7, item.finalPrice());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
            }
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving order to database", e);
        }
        return new OrderDL(generatedId, businessNumber, supplierName, address, phone, items);
    }

    @Override
    public OrderDL getOrder(int orderId) {
        String sqlOrder = "SELECT * FROM Orders WHERE orderId = ?";
        String sqlItems = "SELECT * FROM OrderItems WHERE orderId = ?";
        OrderDL order = null;
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sqlOrder)) {
                pstmt.setInt(1, orderId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    List<OrderItemDL> itemsList = new ArrayList<>();
                    try (PreparedStatement itemsStmt = conn.prepareStatement(sqlItems)) {
                        itemsStmt.setInt(1, orderId);
                        ResultSet itemsRs = itemsStmt.executeQuery();
                        while (itemsRs.next()) {
                            itemsList.add(new OrderItemDL(
                                    itemsRs.getInt("catalogId"),
                                    itemsRs.getString("productName"),
                                    itemsRs.getInt("quantity"),
                                    itemsRs.getDouble("listPrice"),
                                    itemsRs.getDouble("discount"),
                                    itemsRs.getDouble("finalPrice")
                            ));
                        }
                    }
                    order = new OrderDL(orderId, LocalDate.parse(rs.getString("orderDate")), rs.getString("businessNumber"), rs.getString("supplierName"), rs.getString("address"), rs.getString("contactPhone"), itemsList);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading order", e);
        }
        return order;
    }

    @Override
    public Map<Integer, OrderDL> getAllOrders() {
        Map<Integer, OrderDL> orders = new HashMap<>();
        String sql = "SELECT orderId FROM Orders";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("orderId");
                orders.put(id, getOrder(id));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading all orders", e);
        }
        return orders;
    }
}