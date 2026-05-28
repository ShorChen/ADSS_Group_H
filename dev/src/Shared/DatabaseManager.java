package Shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:Super-Lee.db";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        // Supplier Tables
        String suppliersTable = "CREATE TABLE IF NOT EXISTS Suppliers (businessNumber TEXT PRIMARY KEY, name TEXT NOT NULL, address TEXT NOT NULL, bankAccount TEXT NOT NULL, paymentTerms TEXT NOT NULL);";
        String contactsTable = "CREATE TABLE IF NOT EXISTS ContactPersons (phone TEXT PRIMARY KEY, businessNumber TEXT NOT NULL, name TEXT NOT NULL, email TEXT NOT NULL, FOREIGN KEY(businessNumber) REFERENCES Suppliers(businessNumber) ON DELETE CASCADE);";
        String agreementsTable = "CREATE TABLE IF NOT EXISTS Agreements (agreementId INTEGER PRIMARY KEY AUTOINCREMENT, businessNumber TEXT NOT NULL, supplierTransports INTEGER NOT NULL, fixedDays TEXT, FOREIGN KEY(businessNumber) REFERENCES Suppliers(businessNumber) ON DELETE CASCADE);";
        String manufacturersTable = "CREATE TABLE IF NOT EXISTS Manufacturers (businessNumber TEXT NOT NULL, name TEXT NOT NULL, PRIMARY KEY(businessNumber, name), FOREIGN KEY(businessNumber) REFERENCES Suppliers(businessNumber) ON DELETE CASCADE);";

        // Product Line & Discount Tables (Crucial for Agreements)
        String productLinesTable = "CREATE TABLE IF NOT EXISTS ProductLines (agreementId INTEGER, catalogId INTEGER, name TEXT NOT NULL, basePrice REAL NOT NULL, quantity INTEGER NOT NULL, PRIMARY KEY(agreementId, catalogId), FOREIGN KEY(agreementId) REFERENCES Agreements(agreementId) ON DELETE CASCADE);";
        String discountsTable = "CREATE TABLE IF NOT EXISTS Discounts (agreementId INTEGER, catalogId INTEGER, minQuantity INTEGER, discountPercentage REAL, PRIMARY KEY(agreementId, catalogId, minQuantity), FOREIGN KEY(agreementId) REFERENCES Agreements(agreementId) ON DELETE CASCADE);";

        // Auth Table (Fixed to match your interface)
        String authTable = "CREATE TABLE IF NOT EXISTS AuthCodes (code TEXT PRIMARY KEY, role TEXT NOT NULL);";

        // Order Tables
        String ordersTable = "CREATE TABLE IF NOT EXISTS Orders (orderId INTEGER PRIMARY KEY AUTOINCREMENT, businessNumber TEXT NOT NULL, supplierName TEXT, address TEXT, contactPhone TEXT, orderDate TEXT NOT NULL);";
        String orderItemsTable = "CREATE TABLE IF NOT EXISTS OrderItems (orderId INTEGER, catalogId INTEGER, productName TEXT, quantity INTEGER, listPrice REAL, discount REAL, finalPrice REAL, PRIMARY KEY(orderId, catalogId), FOREIGN KEY(orderId) REFERENCES Orders(orderId) ON DELETE CASCADE);";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;"); // Crucial for SQLite cascading deletes

            stmt.execute(suppliersTable);
            stmt.execute(contactsTable);
            stmt.execute(agreementsTable);
            stmt.execute(manufacturersTable);
            stmt.execute(productLinesTable); // Added
            stmt.execute(discountsTable);    // Added
            stmt.execute(authTable);
            stmt.execute(ordersTable);
            stmt.execute(orderItemsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}