package Core.DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings("unused")
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
        String productsTable = "CREATE TABLE IF NOT EXISTS Products (barcode TEXT PRIMARY KEY, name TEXT NOT NULL, manufacturer TEXT NOT NULL, categoryId INTEGER NOT NULL, costPrice REAL NOT NULL, sellingPrice REAL NOT NULL, minQuantity INTEGER NOT NULL, shelfQuantity INTEGER NOT NULL DEFAULT 0, warehouseQuantity INTEGER NOT NULL DEFAULT 0, aisle TEXT NOT NULL, position INTEGER NOT NULL, FOREIGN KEY(categoryId) REFERENCES Categories(categoryId));";
        String defectiveItemsTable = "CREATE TABLE IF NOT EXISTS DefectiveItems (defectId INTEGER PRIMARY KEY AUTOINCREMENT, barcode TEXT NOT NULL, quantity INTEGER NOT NULL, location TEXT NOT NULL, reason TEXT NOT NULL, reportDate TEXT NOT NULL, FOREIGN KEY(barcode) REFERENCES Products(barcode) ON DELETE CASCADE);";
        String promotionsTable = "CREATE TABLE IF NOT EXISTS Promotions (promoId INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, discountPercentage REAL NOT NULL, startDate TEXT NOT NULL, endDate TEXT NOT NULL, targetType TEXT CHECK(targetType IN ('CATEGORY', 'PRODUCT')), targetId TEXT NOT NULL);";
        String trucksTable = "CREATE TABLE IF NOT EXISTS Trucks (licenseNumber TEXT PRIMARY KEY, model TEXT NOT NULL, netWeight REAL NOT NULL, maxWeight REAL NOT NULL, isRefrigerated INTEGER NOT NULL);";
        String driversTable = "CREATE TABLE IF NOT EXISTS Drivers (employeeId TEXT PRIMARY KEY, name TEXT NOT NULL, licenseType TEXT NOT NULL, licenseExpiry TEXT NOT NULL);";
        String sitesTable = "CREATE TABLE IF NOT EXISTS Sites (siteName TEXT PRIMARY KEY, address TEXT NOT NULL, contactPerson TEXT NOT NULL, phoneNumber TEXT NOT NULL);";
        String deliveriesTable = "CREATE TABLE IF NOT EXISTS Deliveries (deliveryId INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT NOT NULL, departureTime TEXT NOT NULL, status TEXT NOT NULL, truckLicense TEXT NOT NULL, driverId TEXT NOT NULL, originSite TEXT NOT NULL, FOREIGN KEY(truckLicense) REFERENCES Trucks(licenseNumber), FOREIGN KEY(driverId) REFERENCES Drivers(employeeId), FOREIGN KEY(originSite) REFERENCES Sites(siteName));";
        String deliveryDestinationsTable = "CREATE TABLE IF NOT EXISTS DeliveryDestinations (destId INTEGER PRIMARY KEY AUTOINCREMENT, deliveryId INTEGER NOT NULL, destinationSite TEXT NOT NULL, routeOrder INTEGER NOT NULL, FOREIGN KEY(deliveryId) REFERENCES Deliveries(deliveryId) ON DELETE CASCADE, FOREIGN KEY(destinationSite) REFERENCES Sites(siteName));";
        String cargoItemsTable = "CREATE TABLE IF NOT EXISTS CargoItems (cargoId INTEGER PRIMARY KEY AUTOINCREMENT, destId INTEGER NOT NULL, itemName TEXT NOT NULL, weight REAL NOT NULL, quantity INTEGER NOT NULL, FOREIGN KEY(destId) REFERENCES DeliveryDestinations(destId) ON DELETE CASCADE);";
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
            stmt.execute(trucksTable);
            stmt.execute(driversTable);
            stmt.execute(sitesTable);
            stmt.execute(deliveriesTable);
            stmt.execute(deliveryDestinationsTable);
            stmt.execute(cargoItemsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void seedDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT OR IGNORE INTO AuthCodes(code, role) VALUES('ORD123', 'ORDER_MANAGER')");
            stmt.execute("INSERT OR IGNORE INTO AuthCodes(code, role) VALUES('INV123', 'INVENTORY_MANAGER')");
            stmt.execute("INSERT OR IGNORE INTO AuthCodes(code, role) VALUES('SUP123', 'SUPPLIER_MANAGER')");
            stmt.execute("INSERT OR IGNORE INTO AuthCodes(code, role) VALUES('TRANS123', 'TRANSPORTATION_MANAGER')");
            stmt.execute("INSERT OR IGNORE INTO AuthCodes(code, role) VALUES('HR123', 'HR_MANAGER')");
            stmt.execute("INSERT OR IGNORE INTO Suppliers(businessNumber, name, address, bankAccount, paymentTerms) VALUES('1111', 'Tnuva', 'Tel Aviv', 'IL1111111111111111111111111', 'NET30')");
            stmt.execute("INSERT OR IGNORE INTO Suppliers(businessNumber, name, address, bankAccount, paymentTerms) VALUES('2222', 'Osem', 'Petah Tikva', 'IL2222222222222222222222222', 'EOM')");
            stmt.execute("INSERT OR IGNORE INTO Suppliers(businessNumber, name, address, bankAccount, paymentTerms) VALUES('3333', 'Strauss', 'Haifa', 'IL3333333333333333333333333', 'NET60')");
            stmt.execute("INSERT OR IGNORE INTO Suppliers(businessNumber, name, address, bankAccount, paymentTerms) VALUES('4444', 'Angel Bakeries', 'Jerusalem', 'IL4444444444444444444444444', 'NET30')");
            stmt.execute("INSERT OR IGNORE INTO ContactPersons(phone, businessNumber, name, email) VALUES('050-1111111', '1111', 'Avi Cohen', 'avi@tnuva.co.il')");
            stmt.execute("INSERT OR IGNORE INTO ContactPersons(phone, businessNumber, name, email) VALUES('052-2222222', '2222', 'Dana Levy', 'dana@osem.co.il')");
            stmt.execute("INSERT OR IGNORE INTO ContactPersons(phone, businessNumber, name, email) VALUES('054-3333333', '3333', 'Yossi Katz', 'yossi@strauss.co.il')");
            stmt.execute("INSERT OR IGNORE INTO ContactPersons(phone, businessNumber, name, email) VALUES('053-4444444', '4444', 'Eli Angel', 'eli@angel.co.il')");
            stmt.execute("INSERT OR IGNORE INTO Agreements(agreementId, businessNumber, supplierTransports, fixedDays) VALUES(1, '1111', 1, 'SUNDAY,WEDNESDAY')");
            stmt.execute("INSERT OR IGNORE INTO Agreements(agreementId, businessNumber, supplierTransports, fixedDays) VALUES(2, '2222', 0, 'TUESDAY')");
            stmt.execute("INSERT OR IGNORE INTO Agreements(agreementId, businessNumber, supplierTransports, fixedDays) VALUES(3, '3333', 1, 'MONDAY')");
            stmt.execute("INSERT OR IGNORE INTO Agreements(agreementId, businessNumber, supplierTransports, fixedDays) VALUES(4, '4444', 1, 'SUNDAY,TUESDAY,THURSDAY')");
            stmt.execute("INSERT OR IGNORE INTO Manufacturers(businessNumber, name) VALUES('1111', 'Tnuva')");
            stmt.execute("INSERT OR IGNORE INTO Manufacturers(businessNumber, name) VALUES('2222', 'Osem')");
            stmt.execute("INSERT OR IGNORE INTO Manufacturers(businessNumber, name) VALUES('3333', 'Strauss')");
            stmt.execute("INSERT OR IGNORE INTO Manufacturers(businessNumber, name) VALUES('4444', 'Angel')");
            stmt.execute("INSERT OR IGNORE INTO ProductLines(agreementId, catalogId, name, basePrice, quantity) VALUES(1, 101, 'Milk 3%', 4.0, 100)");
            stmt.execute("INSERT OR IGNORE INTO ProductLines(agreementId, catalogId, name, basePrice, quantity) VALUES(1, 102, 'Cottage Cheese', 3.5, 100)");
            stmt.execute("INSERT OR IGNORE INTO ProductLines(agreementId, catalogId, name, basePrice, quantity) VALUES(2, 201, 'Bamba', 2.0, 200)");
            stmt.execute("INSERT OR IGNORE INTO ProductLines(agreementId, catalogId, name, basePrice, quantity) VALUES(2, 202, 'Bisli', 2.5, 200)");
            stmt.execute("INSERT OR IGNORE INTO ProductLines(agreementId, catalogId, name, basePrice, quantity) VALUES(3, 301, 'Chocolate Milk', 5.0, 100)");
            stmt.execute("INSERT OR IGNORE INTO ProductLines(agreementId, catalogId, name, basePrice, quantity) VALUES(4, 401, 'Sliced Bread', 6.0, 50)");
            stmt.execute("INSERT OR IGNORE INTO Discounts(agreementId, catalogId, minQuantity, discountPercentage) VALUES(1, 101, 50, 5.0)");
            stmt.execute("INSERT OR IGNORE INTO Discounts(agreementId, catalogId, minQuantity, discountPercentage) VALUES(1, 101, 100, 10.0)");
            stmt.execute("INSERT OR IGNORE INTO Discounts(agreementId, catalogId, minQuantity, discountPercentage) VALUES(2, 201, 100, 15.0)");
            stmt.execute("INSERT OR IGNORE INTO Discounts(agreementId, catalogId, minQuantity, discountPercentage) VALUES(4, 401, 20, 5.0)");
            stmt.execute("INSERT OR IGNORE INTO Orders(orderId, businessNumber, supplierName, address, contactPhone, orderDate) VALUES(1, '1111', 'Tnuva', 'Tel Aviv', '050-1111111', '2026-06-01')");
            stmt.execute("INSERT OR IGNORE INTO Orders(orderId, businessNumber, supplierName, address, contactPhone, orderDate) VALUES(2, '2222', 'Osem', 'Petah Tikva', '052-2222222', '2026-06-02')");
            stmt.execute("INSERT OR IGNORE INTO OrderItems(orderId, catalogId, productName, quantity, listPrice, discount, finalPrice) VALUES(1, 101, 'Milk 3%', 100, 4.0, 10.0, 3.6)");
            stmt.execute("INSERT OR IGNORE INTO OrderItems(orderId, catalogId, productName, quantity, listPrice, discount, finalPrice) VALUES(2, 201, 'Bamba', 200, 2.0, 15.0, 1.7)");
            stmt.execute("INSERT OR IGNORE INTO Categories(categoryId, name, parentId) VALUES(1, 'Dairy', null)");
            stmt.execute("INSERT OR IGNORE INTO Categories(categoryId, name, parentId) VALUES(2, 'Snacks', null)");
            stmt.execute("INSERT OR IGNORE INTO Categories(categoryId, name, parentId) VALUES(3, 'Beverages', null)");
            stmt.execute("INSERT OR IGNORE INTO Categories(categoryId, name, parentId) VALUES(4, 'Bakery', null)");
            stmt.execute("INSERT OR IGNORE INTO Products(barcode, name, manufacturer, categoryId, costPrice, sellingPrice, minQuantity, shelfQuantity, warehouseQuantity, aisle, position) VALUES('1001', 'Milk 3%', 'Tnuva', 1, 4.0, 6.0, 50, 20, 100, 'A1', 1)");
            stmt.execute("INSERT OR IGNORE INTO Products(barcode, name, manufacturer, categoryId, costPrice, sellingPrice, minQuantity, shelfQuantity, warehouseQuantity, aisle, position) VALUES('1002', 'Cottage Cheese', 'Tnuva', 1, 3.5, 5.0, 30, 15, 40, 'A1', 2)");
            stmt.execute("INSERT OR IGNORE INTO Products(barcode, name, manufacturer, categoryId, costPrice, sellingPrice, minQuantity, shelfQuantity, warehouseQuantity, aisle, position) VALUES('2001', 'Bamba', 'Osem', 2, 2.0, 4.5, 40, 25, 80, 'B3', 1)");
            stmt.execute("INSERT OR IGNORE INTO Products(barcode, name, manufacturer, categoryId, costPrice, sellingPrice, minQuantity, shelfQuantity, warehouseQuantity, aisle, position) VALUES('2002', 'Bisli', 'Osem', 2, 2.5, 5.0, 40, 20, 60, 'B3', 2)");
            stmt.execute("INSERT OR IGNORE INTO Products(barcode, name, manufacturer, categoryId, costPrice, sellingPrice, minQuantity, shelfQuantity, warehouseQuantity, aisle, position) VALUES('3001', 'Chocolate Milk', 'Strauss', 3, 5.0, 8.0, 20, 10, 30, 'C2', 1)");
            stmt.execute("INSERT OR IGNORE INTO Products(barcode, name, manufacturer, categoryId, costPrice, sellingPrice, minQuantity, shelfQuantity, warehouseQuantity, aisle, position) VALUES('4001', 'Sliced Bread', 'Angel', 4, 6.0, 10.0, 15, 10, 20, 'D1', 1)");
            stmt.execute("INSERT OR IGNORE INTO DefectiveItems(defectId, barcode, quantity, location, reason, reportDate) VALUES(1, '1001', 5, 'SHELF', 'Expired', '2026-06-04')");
            stmt.execute("INSERT OR IGNORE INTO DefectiveItems(defectId, barcode, quantity, location, reason, reportDate) VALUES(2, '3001', 2, 'WAREHOUSE', 'Broken Container', '2026-06-05')");
            stmt.execute("INSERT OR IGNORE INTO Promotions(promoId, name, discountPercentage, startDate, endDate, targetType, targetId) VALUES(1, 'Summer Dairy Fest', 15.0, '2026-06-01', '2026-08-31', 'CATEGORY', '1')");
            stmt.execute("INSERT OR IGNORE INTO Promotions(promoId, name, discountPercentage, startDate, endDate, targetType, targetId) VALUES(2, 'Bamba Madness', 20.0, '2026-06-01', '2026-06-14', 'PRODUCT', '2001')");
            stmt.execute("INSERT OR IGNORE INTO Trucks(licenseNumber, model, netWeight, maxWeight, isRefrigerated) VALUES('11-222-33', 'Volvo FH16', 8000.0, 24000.0, 1)");
            stmt.execute("INSERT OR IGNORE INTO Trucks(licenseNumber, model, netWeight, maxWeight, isRefrigerated) VALUES('44-555-66', 'Mercedes Actros', 7500.0, 18000.0, 0)");
            stmt.execute("INSERT OR IGNORE INTO Trucks(licenseNumber, model, netWeight, maxWeight, isRefrigerated) VALUES('77-888-99', 'Scania', 5000.0, 12000.0, 1)");
            stmt.execute("INSERT OR IGNORE INTO Drivers(employeeId, name, licenseType, licenseExpiry) VALUES('D001', 'Moshe Levi', 'HEAVY', '2028-12-31')");
            stmt.execute("INSERT OR IGNORE INTO Drivers(employeeId, name, licenseType, licenseExpiry) VALUES('D002', 'David Cohen', 'STANDARD', '2027-05-15')");
            stmt.execute("INSERT OR IGNORE INTO Drivers(employeeId, name, licenseType, licenseExpiry) VALUES('D003', 'Rami Golan', 'HEAVY', '2029-01-01')");
            stmt.execute("INSERT OR IGNORE INTO Sites(siteName, address, contactPerson, phoneNumber) VALUES('Main Logistics Center', '10 HaMelacha St, Ashdod', 'Eran', '050-9998887')");
            stmt.execute("INSERT OR IGNORE INTO Sites(siteName, address, contactPerson, phoneNumber) VALUES('Super-Lee Tel Aviv', '50 Dizengoff St, Tel Aviv', 'Ronit', '052-1112223')");
            stmt.execute("INSERT OR IGNORE INTO Sites(siteName, address, contactPerson, phoneNumber) VALUES('Super-Lee Haifa', '12 Herzl St, Haifa', 'Yael', '054-3334445')");
            stmt.execute("INSERT OR IGNORE INTO Sites(siteName, address, contactPerson, phoneNumber) VALUES('Super-Lee Jerusalem', '8 Jaffa St, Jerusalem', 'Omer', '053-5556667')");
            stmt.execute("INSERT OR IGNORE INTO Deliveries(deliveryId, date, departureTime, status, truckLicense, driverId, originSite) VALUES(1, '2026-06-10', '06:00', 'SCHEDULED', '11-222-33', 'D001', 'Main Logistics Center')");
            stmt.execute("INSERT OR IGNORE INTO Deliveries(deliveryId, date, departureTime, status, truckLicense, driverId, originSite) VALUES(2, '2026-06-11', '08:30', 'IN_TRANSIT', '44-555-66', 'D002', 'Main Logistics Center')");
            stmt.execute("INSERT OR IGNORE INTO DeliveryDestinations(destId, deliveryId, destinationSite, routeOrder) VALUES(1, 1, 'Super-Lee Tel Aviv', 1)");
            stmt.execute("INSERT OR IGNORE INTO DeliveryDestinations(destId, deliveryId, destinationSite, routeOrder) VALUES(2, 1, 'Super-Lee Jerusalem', 2)");
            stmt.execute("INSERT OR IGNORE INTO DeliveryDestinations(destId, deliveryId, destinationSite, routeOrder) VALUES(3, 2, 'Super-Lee Haifa', 1)");
            stmt.execute("INSERT OR IGNORE INTO CargoItems(cargoId, destId, itemName, weight, quantity) VALUES(1, 1, 'Milk Pallets', 500.0, 10)");
            stmt.execute("INSERT OR IGNORE INTO CargoItems(cargoId, destId, itemName, weight, quantity) VALUES(2, 1, 'Cheese Boxes', 200.0, 5)");
            stmt.execute("INSERT OR IGNORE INTO CargoItems(cargoId, destId, itemName, weight, quantity) VALUES(3, 2, 'Bamba Crates', 150.0, 20)");
            stmt.execute("INSERT OR IGNORE INTO CargoItems(cargoId, destId, itemName, weight, quantity) VALUES(4, 3, 'Bread Trays', 300.0, 15)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}