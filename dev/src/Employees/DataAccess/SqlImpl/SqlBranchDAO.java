package Employees.DataAccess.SqlImpl;

import Core.DataAccess.DatabaseManager;
import Employees.DataAccess.BranchDAO;
import Employees.DataAccess.Entities.BranchEntity;
import Employees.DataAccess.Entities.Keys.WeekKey;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlBranchDAO implements BranchDAO {

    @Override
    public List<String> getAllBranchLocations() {
        List<String> locations = new ArrayList<>();
        String sql = "SELECT DISTINCT location FROM Branches";
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) locations.add(rs.getString("location"));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading branch locations", e);
        }
        return locations;
    }

    @Override
    public boolean exists(int branchId) {
        String sql = "SELECT 1 FROM Branches WHERE branchId=? AND location=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, branchId);
            return pstmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking branch", e);
        }
    }

    @Override
    public List<BranchEntity> getAllBranches() {
        List<BranchEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM Branches";
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next())
                list.add(new BranchEntity(rs.getInt("branchId"), rs.getString("location"), rs.getString("branchManagerId"), new WeekKey(rs.getInt("year"), rs.getInt("week"))));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading branches", e);
        }
        return list;
    }

    @Override
    public void addUpdateBranch(BranchEntity branch) {
        String sql = "INSERT OR REPLACE INTO Branches(branchId, location) VALUES(?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, branch.branchId());
            pstmt.setString(2, branch.location());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving branch", e);
        }
    }

    @Override
    public BranchEntity get(int branchId) {
        List<BranchEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM Branches WHERE branchId=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, branchId);
            ResultSet rs = pstmt.executeQuery(sql);
            if (!rs.next()) return null;
            list.add(new BranchEntity(rs.getInt("branchId"), rs.getString("location"), rs.getString("branchManagerId"), new WeekKey(rs.getInt("year"), rs.getInt("week"))));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading branches", e);
        }
        return list.getFirst();
    }
}