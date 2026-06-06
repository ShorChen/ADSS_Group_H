package Transportation.DataAccess.SqlImpl;

import Core.DataAccess.DatabaseManager;
import Transportation.DataAccess.TransportDAO;
import Transportation.Domain.Entities.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SqlTransportDAO implements TransportDAO {

    @Override
    public void addTruck(TruckDL truck) {
        String sql = "INSERT INTO Trucks(licenseNumber, model, netWeight, maxWeight, isRefrigerated) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, truck.getLicenseNumber());
            pstmt.setString(2, truck.getModel());
            pstmt.setDouble(3, truck.getNetWeight());
            pstmt.setDouble(4, truck.getMaxWeight());
            pstmt.setInt(5, truck.isRefrigerated() ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error saving truck", e); }
    }

    @Override
    public TruckDL getTruck(String licenseNumber) {
        String sql = "SELECT * FROM Trucks WHERE licenseNumber=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, licenseNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return new TruckDL(rs.getString("licenseNumber"), rs.getString("model"), rs.getDouble("netWeight"), rs.getDouble("maxWeight"), rs.getInt("isRefrigerated") == 1);
        } catch (SQLException e) { throw new RuntimeException("Error loading truck", e); }
        return null;
    }

    @Override
    public List<TruckDL> getAllTrucks() {
        List<TruckDL> list = new ArrayList<>();
        String sql = "SELECT * FROM Trucks";
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(new TruckDL(rs.getString("licenseNumber"), rs.getString("model"), rs.getDouble("netWeight"), rs.getDouble("maxWeight"), rs.getInt("isRefrigerated") == 1));
        } catch (SQLException e) { throw new RuntimeException("Error loading trucks", e); }
        return list;
    }

    @Override
    public void addDriver(DriverDL driver) {
        String sql = "INSERT INTO Drivers(employeeId, name, licenseType, licenseExpiry) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, driver.getEmployeeId());
            pstmt.setString(2, driver.getName());
            pstmt.setString(3, driver.getLicenseType());
            pstmt.setString(4, driver.getLicenseExpiry().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error saving driver", e); }
    }

    @Override
    public DriverDL getDriver(String employeeId) {
        String sql = "SELECT * FROM Drivers WHERE employeeId=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return new DriverDL(rs.getString("employeeId"), rs.getString("name"), rs.getString("licenseType"), LocalDate.parse(rs.getString("licenseExpiry")));
        } catch (SQLException e) { throw new RuntimeException("Error loading driver", e); }
        return null;
    }

    @Override
    public List<DriverDL> getAllDrivers() {
        List<DriverDL> list = new ArrayList<>();
        String sql = "SELECT * FROM Drivers";
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(new DriverDL(rs.getString("employeeId"), rs.getString("name"), rs.getString("licenseType"), LocalDate.parse(rs.getString("licenseExpiry"))));
        } catch (SQLException e) { throw new RuntimeException("Error loading drivers", e); }
        return list;
    }

    @Override
    public void addSite(SiteDL site) {
        String sql = "INSERT INTO Sites(siteName, address, contactPerson, phoneNumber) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, site.getSiteName());
            pstmt.setString(2, site.getAddress());
            pstmt.setString(3, site.getContactPerson());
            pstmt.setString(4, site.getPhoneNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error saving site", e); }
    }

    @Override
    public SiteDL getSite(String siteName) {
        String sql = "SELECT * FROM Sites WHERE siteName=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, siteName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return new SiteDL(rs.getString("siteName"), rs.getString("address"), rs.getString("contactPerson"), rs.getString("phoneNumber"));
        } catch (SQLException e) { throw new RuntimeException("Error loading site", e); }
        return null;
    }

    @Override
    public List<SiteDL> getAllSites() {
        List<SiteDL> list = new ArrayList<>();
        String sql = "SELECT * FROM Sites";
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(new SiteDL(rs.getString("siteName"), rs.getString("address"), rs.getString("contactPerson"), rs.getString("phoneNumber")));
        } catch (SQLException e) { throw new RuntimeException("Error loading sites", e); }
        return list;
    }

    @Override
    public int addDelivery(DeliveryDL delivery) {
        String sqlDel = "INSERT INTO Deliveries(date, departureTime, status, truckLicense, driverId, originSite) VALUES(?,?,?,?,?,?)";
        String sqlDest = "INSERT INTO DeliveryDestinations(deliveryId, destinationSite, routeOrder) VALUES(?,?,?)";
        String sqlItem = "INSERT INTO CargoItems(destId, itemName, weight, quantity) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmtDel = conn.prepareStatement(sqlDel, Statement.RETURN_GENERATED_KEYS)) {
                stmtDel.setString(1, delivery.getDate().toString());
                stmtDel.setString(2, delivery.getDepartureTime());
                stmtDel.setString(3, delivery.getStatus());
                stmtDel.setString(4, delivery.getTruckLicense());
                stmtDel.setString(5, delivery.getDriverId());
                stmtDel.setString(6, delivery.getOriginSite());
                stmtDel.executeUpdate();
                int delId;
                try (ResultSet rs = stmtDel.getGeneratedKeys()) {
                    if (rs.next()) delId = rs.getInt(1);
                    else throw new SQLException("Failed delivery ID");
                }
                try (PreparedStatement stmtDest = conn.prepareStatement(sqlDest, Statement.RETURN_GENERATED_KEYS);
                     PreparedStatement stmtItem = conn.prepareStatement(sqlItem)) {
                    int order = 1;
                    for (DestinationDL dest : delivery.getDestinations()) {
                        stmtDest.setInt(1, delId);
                        stmtDest.setString(2, dest.getDestinationSite());
                        stmtDest.setInt(3, order++);
                        stmtDest.executeUpdate();
                        int destId;
                        try (ResultSet rs2 = stmtDest.getGeneratedKeys()) {
                            if (rs2.next()) destId = rs2.getInt(1);
                            else throw new SQLException("Failed dest ID");
                        }
                        for (CargoItemDL item : dest.getItems()) {
                            stmtItem.setInt(1, destId);
                            stmtItem.setString(2, item.getItemName());
                            stmtItem.setDouble(3, item.getWeight());
                            stmtItem.setInt(4, item.getQuantity());
                            stmtItem.addBatch();
                        }
                        stmtItem.executeBatch();
                    }
                }
                conn.commit();
                return delId;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) { throw new RuntimeException("Error saving delivery", e); }
    }

    @Override
    public DeliveryDL getDelivery(int deliveryId) {
        String sql = "SELECT * FROM Deliveries WHERE deliveryId=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, deliveryId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                List<DestinationDL> dests = loadDestinations(deliveryId, conn);
                return new DeliveryDL(rs.getInt("deliveryId"), LocalDate.parse(rs.getString("date")), rs.getString("departureTime"), rs.getString("status"), rs.getString("truckLicense"), rs.getString("driverId"), rs.getString("originSite"), dests);
            }
        } catch (SQLException e) { throw new RuntimeException("Error loading delivery", e); }
        return null;
    }

    @Override
    public List<DeliveryDL> getAllDeliveries() {
        List<DeliveryDL> list = new ArrayList<>();
        String sql = "SELECT * FROM Deliveries";
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int delId = rs.getInt("deliveryId");
                List<DestinationDL> dests = loadDestinations(delId, conn);
                list.add(new DeliveryDL(delId, LocalDate.parse(rs.getString("date")), rs.getString("departureTime"), rs.getString("status"), rs.getString("truckLicense"), rs.getString("driverId"), rs.getString("originSite"), dests));
            }
        } catch (SQLException e) { throw new RuntimeException("Error loading deliveries", e); }
        return list;
    }

    @Override
    public void updateDeliveryStatus(int deliveryId, String status) {
        String sql = "UPDATE Deliveries SET status=? WHERE deliveryId=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, deliveryId);
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error updating delivery status", e); }
    }

    private List<DestinationDL> loadDestinations(int deliveryId, Connection conn) throws SQLException {
        List<DestinationDL> dests = new ArrayList<>();
        String sql = "SELECT * FROM DeliveryDestinations WHERE deliveryId=? ORDER BY routeOrder ASC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, deliveryId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int destId = rs.getInt("destId");
                List<CargoItemDL> items = loadCargoItems(destId, conn);
                dests.add(new DestinationDL(rs.getString("destinationSite"), items));
            }
        }
        return dests;
    }

    private List<CargoItemDL> loadCargoItems(int destId, Connection conn) throws SQLException {
        List<CargoItemDL> items = new ArrayList<>();
        String sql = "SELECT * FROM CargoItems WHERE destId=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, destId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) items.add(new CargoItemDL(rs.getString("itemName"), rs.getDouble("weight"), rs.getInt("quantity")));
        }
        return items;
    }

    @Override
    public void updateDelivery(DeliveryDL delivery) {
        String sql = "UPDATE Deliveries SET status=?, truckLicense=?, driverId=? WHERE deliveryId=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, delivery.getStatus());
            pstmt.setString(2, delivery.getTruckLicense());
            pstmt.setString(3, delivery.getDriverId());
            pstmt.setInt(4, delivery.getDeliveryId());
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error updating delivery", e); }
    }
}