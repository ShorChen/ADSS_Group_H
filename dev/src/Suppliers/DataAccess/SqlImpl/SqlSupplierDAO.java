package Suppliers.DataAccess.SqlImpl;

import Shared.DatabaseManager;
import Suppliers.DataAccess.SupplierDAO;
import Suppliers.Domain.Entities.SupplierDL;
import Suppliers.Domain.Entities.AgreementDL;
import Suppliers.Domain.ValueObjects.PaymentDetails;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlSupplierDAO implements SupplierDAO {

    @Override
    public void addSupplier(SupplierDL supplier) {
        String sql = "INSERT INTO Suppliers(businessNumber, name, address, bankAccount, paymentTerms) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getBusinessNumber());
            pstmt.setString(2, supplier.getName());
            pstmt.setString(3, supplier.getAddress());
            pstmt.setString(4, supplier.getPaymentDetails().getBankAccount().toString());
            pstmt.setString(5, supplier.getPaymentDetails().getPaymentTerms());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving supplier to database", e);
        }
    }

    @Override
    public void updateSupplier(SupplierDL supplier) {
        String sql = "UPDATE Suppliers SET name = ?, address = ?, bankAccount = ?, paymentTerms = ? WHERE businessNumber = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getAddress());
            pstmt.setString(3, supplier.getPaymentDetails().getBankAccount().toString());
            pstmt.setString(4, supplier.getPaymentDetails().getPaymentTerms());
            pstmt.setString(5, supplier.getBusinessNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating supplier", e);
        }
    }

    @Override
    public void deleteSupplier(String businessNumber) {
        String sql = "DELETE FROM Suppliers WHERE businessNumber = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, businessNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting supplier", e);
        }
    }

    @Override
    public SupplierDL getSupplier(String businessNumber) {
        String sql = "SELECT * FROM Suppliers WHERE businessNumber = ?";
        SupplierDL supplier = null;
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, businessNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                PaymentDetails pd = new PaymentDetails(rs.getString("bankAccount"), rs.getString("paymentTerms"));
                supplier = new SupplierDL(rs.getString("name"), rs.getString("businessNumber"), rs.getString("address"), pd);
                loadContactsForSupplier(conn, supplier);
                loadAgreementsForSupplier(conn, supplier);
                loadManufacturersForSupplier(conn, supplier);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading supplier", e);
        }
        return supplier;
    }

    @Override
    public Map<String, SupplierDL> getAllSuppliers() {
        Map<String, SupplierDL> map = new HashMap<>();
        String sql = "SELECT businessNumber FROM Suppliers";
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String bn = rs.getString("businessNumber");
                map.put(bn, getSupplier(bn));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading all suppliers", e);
        }
        return map;
    }

    // --- Contact Persons ---
    @Override
    public void addContactPersonToDb(String businessNumber, String name, String phone, String email) {
        String sql = "INSERT INTO ContactPersons(phone, businessNumber, name, email) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            pstmt.setString(2, businessNumber);
            pstmt.setString(3, name);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving Contact", e);
        }
    }

    @Override
    public void removeContactPersonFromDb(String phone) {
        String sql = "DELETE FROM ContactPersons WHERE phone = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Contact", e);
        }
    }

    @Override
    public void updateContactNameInDb(String phone, String newName) {
        String sql = "UPDATE ContactPersons SET name = ? WHERE phone = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, phone);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Contact Name", e);
        }
    }

    @Override
    public void updateContactPhoneInDb(String oldPhone, String newPhone) {
        String sql = "UPDATE ContactPersons SET phone = ? WHERE phone = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPhone);
            pstmt.setString(2, oldPhone);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Contact Phone", e);
        }
    }

    @Override
    public void updateContactEmailInDb(String phone, String newEmail) {
        String sql = "UPDATE ContactPersons SET email = ? WHERE phone = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmail);
            pstmt.setString(2, phone);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Contact Email", e);
        }
    }

    // --- Agreements ---
    @Override
    public int addAgreementToDb(String businessNumber, List<DayOfWeek> fixedDeliveryDays, boolean supplierTransports) {
        String sql = "INSERT INTO Agreements(businessNumber, supplierTransports, fixedDays) VALUES(?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, businessNumber);
            pstmt.setInt(2, supplierTransports ? 1 : 0);
            pstmt.setString(3, fixedDeliveryDays.stream().map(Enum::name).collect(java.util.stream.Collectors.joining(",")));
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving agreement to DB", e);
        }
        throw new RuntimeException("Failed to generate Agreement ID");
    }

    @Override
    public void removeAgreementFromDb(int agreementId) {
        String sql = "DELETE FROM Agreements WHERE agreementId = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, agreementId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting agreement", e);
        }
    }

    @Override
    public void updateAgreementDaysInDb(int agreementId, List<DayOfWeek> fixedDeliveryDays) {
        String sql = "UPDATE Agreements SET fixedDays = ? WHERE agreementId = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fixedDeliveryDays.stream().map(Enum::name).collect(java.util.stream.Collectors.joining(",")));
            pstmt.setInt(2, agreementId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating agreement days", e);
        }
    }

    @Override
    public void updateAgreementTransportsInDb(int agreementId, boolean transports) {
        String sql = "UPDATE Agreements SET supplierTransports = ? WHERE agreementId = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transports ? 1 : 0);
            pstmt.setInt(2, agreementId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating agreement transports", e);
        }
    }

    // --- Product Lines ---
    @Override
    public void addProductLineToDb(int agreementId, int catalogId, String name, double basePrice, int quantity) {
        String sql = "INSERT INTO ProductLines(agreementId, catalogId, name, basePrice, quantity) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, agreementId);
            pstmt.setInt(2, catalogId);
            pstmt.setString(3, name);
            pstmt.setDouble(4, basePrice);
            pstmt.setInt(5, quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving Product Line", e);
        }
    }

    @Override
    public void removeProductLineFromDb(int agreementId, int catalogId) {
        String sql = "DELETE FROM ProductLines WHERE agreementId = ? AND catalogId = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, agreementId);
            pstmt.setInt(2, catalogId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product line", e);
        }
    }

    @Override
    public void updateProductLineBasePriceInDb(int agreementId, int catalogId, double newPrice) {
        String sql = "UPDATE ProductLines SET basePrice = ? WHERE agreementId = ? AND catalogId = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, agreementId);
            pstmt.setInt(3, catalogId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product price", e);
        }
    }

    @Override
    public void updateProductLineQuantityInDb(int agreementId, int catalogId, int newQuantity) {
        String sql = "UPDATE ProductLines SET quantity = ? WHERE agreementId = ? AND catalogId = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, agreementId);
            pstmt.setInt(3, catalogId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product quantity", e);
        }
    }

    // --- Discounts ---
    @Override
    public void addDiscountToDb(int agreementId, int catalogId, int minQuantity, double discountPercentage) {
        String sql = "INSERT INTO Discounts(agreementId, catalogId, minQuantity, discountPercentage) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, agreementId);
            pstmt.setInt(2, catalogId);
            pstmt.setInt(3, minQuantity);
            pstmt.setDouble(4, discountPercentage);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving Discount", e);
        }
    }

    @Override
    public void removeDiscountFromDb(int agreementId, int catalogId, int minQuantity) {
        String sql = "DELETE FROM Discounts WHERE agreementId = ? AND catalogId = ? AND minQuantity = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, agreementId);
            pstmt.setInt(2, catalogId);
            pstmt.setInt(3, minQuantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting discount", e);
        }
    }

    @Override
    public void updateDiscountInDb(int agreementId, int catalogId, int minQuantity, double newDiscountPercentage) {
        String sql = "UPDATE Discounts SET discountPercentage = ? WHERE agreementId = ? AND catalogId = ? AND minQuantity = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newDiscountPercentage);
            pstmt.setInt(2, agreementId);
            pstmt.setInt(3, catalogId);
            pstmt.setInt(4, minQuantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating discount", e);
        }
    }

    @Override
    public void addManufacturerToDb(String businessNumber, String manufacturer) {
        String sql = "INSERT INTO Manufacturers(businessNumber, name) VALUES(?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, businessNumber);
            pstmt.setString(2, manufacturer);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving Manufacturer", e);
        }
    }

    @Override
    public void removeManufacturerFromDb(String businessNumber, String manufacturer) {
        String sql = "DELETE FROM Manufacturers WHERE businessNumber = ? AND name = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, businessNumber);
            pstmt.setString(2, manufacturer);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Manufacturer", e);
        }
    }

    // --- Loaders ---
    private void loadContactsForSupplier(Connection conn, SupplierDL supplier) throws SQLException {
        String sql = "SELECT * FROM ContactPersons WHERE businessNumber = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getBusinessNumber());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
                supplier.addContactPerson(rs.getString("name"), rs.getString("phone"), rs.getString("email"));
        }
    }

    private void loadAgreementsForSupplier(Connection conn, SupplierDL supplier) throws SQLException {
        String sql = "SELECT * FROM Agreements WHERE businessNumber = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getBusinessNumber());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int agreementId = rs.getInt("agreementId");
                boolean transports = rs.getInt("supplierTransports") == 1;
                String daysStr = rs.getString("fixedDays");
                List<DayOfWeek> days = new ArrayList<>();
                if (daysStr != null && !daysStr.isEmpty()) {
                    for (String d : daysStr.split(",")) days.add(DayOfWeek.valueOf(d));
                }
                AgreementDL agreement = supplier.addAgreement(agreementId, days, transports);
                loadProductsForAgreement(conn, agreement);
                loadDiscountsForAgreement(conn, agreement);
            }
        }
    }

    private void loadProductsForAgreement(Connection conn, AgreementDL agreement) throws SQLException {
        String sql = "SELECT * FROM ProductLines WHERE agreementId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, agreement.getAgreementId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
                agreement.addProductLine(rs.getInt("catalogId"), rs.getString("name"), rs.getDouble("basePrice"), rs.getInt("quantity"));
        }
    }

    private void loadDiscountsForAgreement(Connection conn, AgreementDL agreement) throws SQLException {
        String sql = "SELECT * FROM Discounts WHERE agreementId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, agreement.getAgreementId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
                agreement.addDiscount(rs.getInt("catalogId"), rs.getInt("minQuantity"), rs.getDouble("discountPercentage"));
        }
    }

    private void loadManufacturersForSupplier(Connection conn, SupplierDL supplier) throws SQLException {
        String sql = "SELECT * FROM Manufacturers WHERE businessNumber = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getBusinessNumber());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) supplier.addManufacturer(rs.getString("name"));
        }
    }
}