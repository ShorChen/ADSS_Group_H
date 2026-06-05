package Inventory.DataAccess.SqlImpl;

import Core.DataAccess.DatabaseManager;
import Inventory.DataAccess.InventoryDAO;
import Inventory.Domain.Entities.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlInventoryDAO implements InventoryDAO {

    @Override
    public void addProduct(ProductDL product) {
        String sql = "INSERT INTO Products(barcode, name, manufacturer, categoryId, costPrice, sellingPrice, minQuantity, shelfQuantity, warehouseQuantity, aisle, position) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getBarcode());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getManufacturer());
            pstmt.setInt(4, product.getCategoryId());
            pstmt.setDouble(5, product.getCostPrice());
            pstmt.setDouble(6, product.getSellingPrice());
            pstmt.setInt(7, product.getMinQuantity());
            pstmt.setInt(8, product.getShelfQuantity());
            pstmt.setInt(9, product.getWarehouseQuantity());
            pstmt.setString(10, product.getAisle());
            pstmt.setInt(11, product.getPosition());
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error saving product", e); }
    }

    @Override
    public void updateProduct(ProductDL product) {
        String sql = "UPDATE Products SET name=?, manufacturer=?, categoryId=?, costPrice=?, sellingPrice=?, minQuantity=?, shelfQuantity=?, warehouseQuantity=?, aisle=?, position=? WHERE barcode=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getManufacturer());
            pstmt.setInt(3, product.getCategoryId());
            pstmt.setDouble(4, product.getCostPrice());
            pstmt.setDouble(5, product.getSellingPrice());
            pstmt.setInt(6, product.getMinQuantity());
            pstmt.setInt(7, product.getShelfQuantity());
            pstmt.setInt(8, product.getWarehouseQuantity());
            pstmt.setString(9, product.getAisle());
            pstmt.setInt(10, product.getPosition());
            pstmt.setString(11, product.getBarcode());
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error updating product", e); }
    }

    @Override
    public void deleteProduct(String barcode) {
        String sql = "DELETE FROM Products WHERE barcode=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, barcode);
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error deleting product", e); }
    }

    @Override
    public ProductDL getProduct(String barcode) {
        String sql = "SELECT * FROM Products WHERE barcode=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, barcode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new ProductDL(rs.getString("barcode"), rs.getString("name"), rs.getString("manufacturer"),
                        rs.getInt("categoryId"), rs.getDouble("costPrice"), rs.getDouble("sellingPrice"),
                        rs.getInt("minQuantity"), rs.getInt("shelfQuantity"), rs.getInt("warehouseQuantity"),
                        rs.getString("aisle"), rs.getInt("position"));
            }
        } catch (SQLException e) { throw new RuntimeException("Error loading product", e); }
        return null;
    }

    @Override
    public Map<String, ProductDL> getAllProducts() {
        Map<String, ProductDL> map = new HashMap<>();
        String sql = "SELECT * FROM Products";
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getString("barcode"), new ProductDL(rs.getString("barcode"), rs.getString("name"), rs.getString("manufacturer"),
                        rs.getInt("categoryId"), rs.getDouble("costPrice"), rs.getDouble("sellingPrice"),
                        rs.getInt("minQuantity"), rs.getInt("shelfQuantity"), rs.getInt("warehouseQuantity"),
                        rs.getString("aisle"), rs.getInt("position")));
            }
        } catch (SQLException e) { throw new RuntimeException("Error loading all products", e); }
        return map;
    }

    @Override
    public int addCategory(CategoryDL category) {
        String sql = "INSERT INTO Categories(name, parentId) VALUES(?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, category.getName());
            pstmt.setObject(2, category.getParentId(), Types.INTEGER);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) { throw new RuntimeException("Error saving category", e); }
        throw new RuntimeException("Failed to generate Category ID");
    }

    @Override
    public CategoryDL getCategory(int categoryId) {
        String sql = "SELECT * FROM Categories WHERE categoryId=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Integer parentId = rs.getObject("parentId") != null ? rs.getInt("parentId") : null;
                return new CategoryDL(rs.getInt("categoryId"), rs.getString("name"), parentId);
            }
        } catch (SQLException e) { throw new RuntimeException("Error loading category", e); }
        return null;
    }

    @Override
    public Map<Integer, CategoryDL> getAllCategories() {
        Map<Integer, CategoryDL> map = new HashMap<>();
        String sql = "SELECT * FROM Categories";
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Integer parentId = rs.getObject("parentId") != null ? rs.getInt("parentId") : null;
                map.put(rs.getInt("categoryId"), new CategoryDL(rs.getInt("categoryId"), rs.getString("name"), parentId));
            }
        } catch (SQLException e) { throw new RuntimeException("Error loading all categories", e); }
        return map;
    }

    @Override
    public void deleteCategory(int categoryId) {
        String sql = "DELETE FROM Categories WHERE categoryId=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error deleting category", e); }
    }

    @Override
    public void addDefectiveItem(DefectiveItemDL item) {
        String sql = "INSERT INTO DefectiveItems(barcode, quantity, location, reason, reportDate) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getBarcode());
            pstmt.setInt(2, item.getQuantity());
            pstmt.setString(3, item.getLocation());
            pstmt.setString(4, item.getReason());
            pstmt.setString(5, item.getReportDate().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error saving defective item", e); }
    }

    @Override
    public List<DefectiveItemDL> getAllDefectiveItems() {
        List<DefectiveItemDL> list = new ArrayList<>();
        String sql = "SELECT * FROM DefectiveItems";
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new DefectiveItemDL(rs.getInt("defectId"), rs.getString("barcode"), rs.getInt("quantity"),
                        rs.getString("location"), rs.getString("reason"), LocalDate.parse(rs.getString("reportDate"))));
            }
        } catch (SQLException e) { throw new RuntimeException("Error loading defective items", e); }
        return list;
    }

    @Override
    public int addPromotion(PromotionDL promo) {
        String sql = "INSERT INTO Promotions(name, discountPercentage, startDate, endDate, targetType, targetId) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, promo.getName());
            pstmt.setDouble(2, promo.getDiscountPercentage());
            pstmt.setString(3, promo.getStartDate().toString());
            pstmt.setString(4, promo.getEndDate().toString());
            pstmt.setString(5, promo.getTargetType());
            pstmt.setString(6, promo.getTargetId());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) { throw new RuntimeException("Error saving promotion", e); }
        throw new RuntimeException("Failed to generate Promotion ID");
    }

    @Override
    public List<PromotionDL> getAllPromotions() {
        List<PromotionDL> list = new ArrayList<>();
        String sql = "SELECT * FROM Promotions";
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new PromotionDL(rs.getInt("promoId"), rs.getString("name"), rs.getDouble("discountPercentage"),
                        LocalDate.parse(rs.getString("startDate")), LocalDate.parse(rs.getString("endDate")),
                        rs.getString("targetType"), rs.getString("targetId")));
            }
        } catch (SQLException e) { throw new RuntimeException("Error loading promotions", e); }
        return list;
    }

    @Override
    public void deletePromotion(int promoId) {
        String sql = "DELETE FROM Promotions WHERE promoId=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, promoId);
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error deleting promotion", e); }
    }
}