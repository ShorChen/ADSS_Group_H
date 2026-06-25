package Employees.DataAccess.SqlImpl;

import Core.DataAccess.DatabaseManager;
import Employees.DataAccess.StoreDAO;
import Employees.Domain.Entities.StoreDetailsDL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStoreDAO implements StoreDAO {

    @Override
    public StoreDetailsDL getStoreDetails() {
        boolean firstStartUp = true;
        List<String> closedDays = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection()) {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT firstStartUp FROM StoreSettings WHERE id=1")) {
                if (rs.next()) firstStartUp = rs.getInt("firstStartUp") == 1;
            }

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT day FROM StoreClosedDays")) {
                while (rs.next()) closedDays.add(rs.getString("day"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching store details", e);
        }

        return new StoreDetailsDL(firstStartUp, closedDays);
    }

    @Override
    public void updateStoreDetails(StoreDetailsDL details) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement pstmt = conn.prepareStatement("INSERT OR REPLACE INTO StoreSettings(id, firstStartUp) VALUES(1, ?)")) {
                    pstmt.setInt(1, details.isFirstStartUp() ? 1 : 0);
                    pstmt.executeUpdate();
                }

                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("DELETE FROM StoreClosedDays");
                }

                if (details.getClosedDays() != null && !details.getClosedDays().isEmpty()) {
                    String sql = "INSERT INTO StoreClosedDays(day) VALUES(?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        for (String day : details.getClosedDays()) {
                            pstmt.setString(1, day);
                            pstmt.addBatch();
                        }
                        pstmt.executeBatch();
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving store details", e);
        }
    }
}