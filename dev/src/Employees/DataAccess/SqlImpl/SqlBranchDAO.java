package Employees.DataAccess.SqlImpl;

import Core.DataAccess.DatabaseManager;
import Employees.DataAccess.BranchDAO;
import Employees.Domain.Entities.BranchDL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlBranchDAO implements BranchDAO {

    @Override
    public List<BranchDL> getAllBranches() {
        List<BranchDL> list = new ArrayList<>();
        String sql = "SELECT * FROM Branches";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next())
                list.add(new BranchDL(
                        rs.getInt("branchId"),
                        rs.getString("location"),
                        rs.getString("branchManagerId"),
                        rs.getInt("year"),
                        rs.getInt("week")
                ));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading branches", e);
        }
        return list;
    }

    @Override
    public void addUpdateBranch(BranchDL branch) {
        String sql = "INSERT OR REPLACE INTO Branches(branchId, location, branchManagerId, year, week) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, branch.getBranchId());
            pstmt.setString(2, branch.getLocation());
            pstmt.setString(3, branch.getBranchManagerId());
            pstmt.setInt(4, branch.getYear());
            pstmt.setInt(5, branch.getWeek());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving branch", e);
        }
    }
}