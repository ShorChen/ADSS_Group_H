package Core.DataAccess.SqlImpl;

import Core.DataAccess.AuthDAO;
import Core.DataAccess.DatabaseManager;
import Core.Domain.Managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SqlAuthDAO implements AuthDAO {

    @Override
    public void addCode(String code, Managers managers) {
        String sql = "INSERT INTO AuthCodes(code, role) VALUES(?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            pstmt.setString(2, managers.name());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving auth code", e);
        }
    }

    @Override
    public void removeCode(String code) {
        String sql = "DELETE FROM AuthCodes WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting auth code", e);
        }
    }

    @Override
    public void updateCode(String code, Managers managers) {
        String sql = "UPDATE AuthCodes SET role = ? WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, managers.name());
            pstmt.setString(2, code);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating auth code", e);
        }
    }

    @Override
    public Managers getRole(String code) {
        String sql = "SELECT role FROM AuthCodes WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Managers.valueOf(rs.getString("role"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching role", e);
        }
        return null;
    }

    @Override
    public Map<String, Managers> getAllCodes() {
        Map<String, Managers> codes = new HashMap<>();
        String sql = "SELECT * FROM AuthCodes";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                codes.put(rs.getString("code"), Managers.valueOf(rs.getString("role")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all codes", e);
        }
        return codes;
    }
}