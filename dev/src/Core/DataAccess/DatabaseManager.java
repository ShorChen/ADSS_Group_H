package Core.DataAccess;

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
    public static void createTables() {
        String suppliersTable = "CREATE TABLE IF NOT EXISTS Suppliers (businessNumber TEXT PRIMARY KEY, name TEXT NOT NULL, address TEXT NOT NULL, bankAccount TEXT NOT NULL, paymentTerms TEXT NOT NULL);";
        String contactsTable = "CREATE TABLE IF NOT EXISTS ContactPersons (phone TEXT PRIMARY KEY, businessNumber TEXT NOT NULL, name TEXT NOT NULL, email TEXT NOT NULL, FOREIGN KEY(businessNumber) REFERENCES Suppliers(businessNumber) ON DELETE CASCADE);";
        String agreementsTable = "CREATE TABLE IF NOT EXISTS Agreements (agreementId INTEGER PRIMARY KEY AUTOINCREMENT, businessNumber TEXT NOT NULL, supplierTransports INTEGER NOT NULL, fixedDays TEXT, FOREIGN KEY(businessNumber) REFERENCES Suppliers(businessNumber) ON DELETE CASCADE);";
        String manufacturersTable = "CREATE TABLE IF NOT EXISTS Manufacturers (businessNumber TEXT NOT NULL, name TEXT NOT NULL, PRIMARY KEY(businessNumber, name), FOREIGN KEY(businessNumber) REFERENCES Suppliers(businessNumber) ON DELETE CASCADE);";
        String productLinesTable = "CREATE TABLE IF NOT EXISTS ProductLines (agreementId INTEGER, catalogId INTEGER, name TEXT NOT NULL, basePrice REAL NOT NULL, quantity INTEGER NOT NULL, PRIMARY KEY(agreementId, catalogId), FOREIGN KEY(agreementId) REFERENCES Agreements(agreementId) ON DELETE CASCADE);";
        String discountsTable = "CREATE TABLE IF NOT EXISTS Discounts (agreementId INTEGER, catalogId INTEGER, minQuantity INTEGER, discountPercentage REAL, PRIMARY KEY(agreementId, catalogId, minQuantity), FOREIGN KEY(agreementId) REFERENCES Agreements(agreementId) ON DELETE CASCADE);";
        String authTable = "CREATE TABLE IF NOT EXISTS AuthCodes (code TEXT PRIMARY KEY, role TEXT NOT NULL);";
        String ordersTable = "CREATE TABLE IF NOT EXISTS Orders (orderId INTEGER PRIMARY KEY AUTOINCREMENT, businessNumber TEXT NOT NULL, supplierName TEXT, address TEXT, contactPhone TEXT, orderDate TEXT NOT NULL);";
        String orderItemsTable = "CREATE TABLE IF NOT EXISTS OrderItems (orderId INTEGER, catalogId INTEGER, productName TEXT, quantity INTEGER, listPrice REAL, discount REAL, finalPrice REAL, PRIMARY KEY(orderId, catalogId), FOREIGN KEY(orderId) REFERENCES Orders(orderId) ON DELETE CASCADE);";

        String categoriesTable = "CREATE TABLE IF NOT EXISTS Categories (categoryId INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, parentId INTEGER, FOREIGN KEY(parentId) REFERENCES Categories(categoryId) ON DELETE CASCADE);";
        String productsTable = "CREATE TABLE IF NOT EXISTS Products (barcode TEXT PRIMARY KEY, name TEXT NOT NULL, manufacturer TEXT NOT NULL, categoryId INTEGER NOT NULL, costPrice REAL NOT NULL, sellingPrice REAL NOT NULL, minQuantity INTEGER NOT NULL, shelfQuantity INTEGER NOT NULL DEFAULT 0, warehouseQuantity INTEGER NOT NULL DEFAULT 0, aisle TEXT NOT NULL, position INTEGER NOT NULL, FOREIGN KEY(categoryId) REFERENCES Categories(categoryId));";        String defectiveItemsTable = "CREATE TABLE IF NOT EXISTS DefectiveItems (defectId INTEGER PRIMARY KEY AUTOINCREMENT, barcode TEXT NOT NULL, quantity INTEGER NOT NULL, location TEXT NOT NULL, reason TEXT NOT NULL, reportDate TEXT NOT NULL, FOREIGN KEY(barcode) REFERENCES Products(barcode) ON DELETE CASCADE);";
        String promotionsTable = "CREATE TABLE IF NOT EXISTS Promotions (promoId INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, discountPercentage REAL NOT NULL, startDate TEXT NOT NULL, endDate TEXT NOT NULL, targetType TEXT CHECK(targetType IN ('CATEGORY', 'PRODUCT')), targetId TEXT NOT NULL);";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(suppliersTable);
            stmt.execute(contactsTable);
            stmt.execute(agreementsTable);
            stmt.execute(manufacturersTable);
            stmt.execute(productLinesTable);
            stmt.execute(discountsTable);
            stmt.execute(authTable);
            stmt.execute(ordersTable);
            stmt.execute(orderItemsTable);
            stmt.execute(categoriesTable);
            stmt.execute(productsTable);
            stmt.execute(defectiveItemsTable);
            stmt.execute(promotionsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}