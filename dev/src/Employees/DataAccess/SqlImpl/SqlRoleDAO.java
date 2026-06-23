package Employees.DataAccess.SqlImpl;

import Core.DataAccess.DatabaseManager;
import Employees.DataAccess.RoleDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlRoleDAO implements RoleDAO {

    public SqlRoleDAO() {
        initDefaultRoles();
    }

    private void initDefaultRoles() {
        String[] defaultRoles = {"Manager", "Branch Manager", "Driver", "Storekeeper", "Shift Manager", "Cashier"};
        String sql = "INSERT OR IGNORE INTO Roles(roleName) VALUES(?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String role : defaultRoles) {
                pstmt.setString(1, role);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing default roles", e);
        }
    }

    @Override
    public boolean containsRole(String role) {
        String sql = "SELECT 1 FROM Roles WHERE roleName=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, role);
            return pstmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking role existence", e);
        }
    }

    @Override
    public void addRole(String role) {
        String sql = "INSERT OR IGNORE INTO Roles(roleName) VALUES(?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, role);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding role", e);
        }
    }

    @Override
    public List<String> getAllRoles() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT roleName FROM Roles";
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("roleName"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading all roles", e);
        }
        return list;
    }
}