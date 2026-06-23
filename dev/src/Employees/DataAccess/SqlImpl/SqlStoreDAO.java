package Employees.DataAccess.SqlImpl;

import Core.DataAccess.DatabaseManager;
import Employees.DataAccess.StoreDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStoreDAO implements StoreDAO {

    @Override
    public List<String> getClosedDays() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT day FROM StoreClosedDays";
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("day"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading closed days", e);
        }
        return list;
    }

    @Override
    public void setClosedDays(List<String> closedDays) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM StoreClosedDays");
            }

            if (closedDays != null && !closedDays.isEmpty()) {
                String sql = "INSERT INTO StoreClosedDays(day) VALUES(?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    for (String day : closedDays) {
                        pstmt.setString(1, day);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving closed days", e);
        }
    }

    @Override
    public boolean isFirstStartUp() {
        String sql = "SELECT firstStartUp FROM StoreSettings WHERE id=1";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("firstStartUp") == 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading store settings", e);
        }
        return true;
    }

    @Override
    public void setFirstStartUp(boolean firstStartUp) {
        String sql = "INSERT OR REPLACE INTO StoreSettings(id, firstStartUp) VALUES(1, ?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, firstStartUp ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving first startup status", e);
        }
    }
}