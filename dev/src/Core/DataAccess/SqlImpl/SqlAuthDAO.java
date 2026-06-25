package Core.DataAccess.SqlImpl;

import Core.DataAccess.AuthDAO;
import Core.DataAccess.DatabaseManager;
import Core.Domain.Role;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SqlAuthDAO implements AuthDAO {

    @Override
    public void addAuthAccount(String employeeId, String password, Role role) {
        String sql = "INSERT INTO EmployeeAuth(employeeId, password, systemRole) VALUES(?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            pstmt.setString(2, password);
            pstmt.setString(3, role.name());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving auth account", e);
        }
    }

    @Override
    public void removeAuthAccount(String employeeId) {
        String sql = "DELETE FROM EmployeeAuth WHERE employeeId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting auth account", e);
        }
    }

    @Override
    public void updateAuthRole(String employeeId, Role role) {
        String sql = "UPDATE EmployeeAuth SET systemRole = ? WHERE employeeId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, role.name());
            pstmt.setString(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating auth role", e);
        }
    }

    @Override
    public void updatePassword(String employeeId, String newPassword) {
        String sql = "UPDATE EmployeeAuth SET password = ? WHERE employeeId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating password", e);
        }
    }

    @Override
    public Role getRole(String employeeId) {
        String sql = "SELECT systemRole FROM EmployeeAuth WHERE employeeId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Role.valueOf(rs.getString("systemRole"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching role", e);
        }
        return null;
    }

    @Override
    public Map<String, Role> getAllAuthAccounts() {
        Map<String, Role> accounts = new HashMap<>();
        String sql = "SELECT employeeId, systemRole FROM EmployeeAuth";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                accounts.put(rs.getString("employeeId"), Role.valueOf(rs.getString("systemRole")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all auth accounts", e);
        }
        return accounts;
    }

    @Override
    public Role login(String id, String password) {
        String query = "SELECT systemRole, password FROM EmployeeAuth WHERE employeeId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                String systemRole = rs.getString("systemRole");
                if (password.equals(dbPassword)) {
                    return Role.valueOf(systemRole.toUpperCase());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Database error during authentication: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean verifyPassword(String id, String password) {
        String sql = "SELECT password FROM Passwords WHERE employeeId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getString("password").equals(password);
        } catch (Exception e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public String getFirstUserIdByRole(Role role) {
        String sql = "SELECT employeeId FROM EmployeeAuth WHERE systemRole = ? LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, role.name());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("employeeId");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user by role", e);
        }
        return null;
    }
}