package Employees.DataAccess.SqlImpl;

import Core.DataAccess.DatabaseManager;
import Employees.DataAccess.ShiftDAO;
import Employees.DataAccess.Entities.ShiftEntity;
import Employees.DataAccess.Entities.Keys.BranchWeekKey;
import Employees.DataAccess.Entities.Keys.ShiftEntityKey;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class SqlShiftDAO implements ShiftDAO {

    @Override
    public List<ShiftEntity> getShiftsByBranchAndWeek(int branchId, int year, int week) {
        List<ShiftEntity> shifts = new ArrayList<>();
        String sql = "SELECT shiftId FROM Shifts WHERE branchId=? AND year=? AND week=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, branchId);
            pstmt.setInt(2, year);
            pstmt.setInt(3, week);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                shifts.add(getShiftById(rs.getInt("shiftId")));
            }
        } catch (SQLException e) { throw new RuntimeException("Error loading shifts", e); }
        return shifts;
    }

    @Override
    public void addUpdateShift(BranchWeekKey branchWeekKey, ShiftEntityKey shiftEntityKey, ShiftEntity shift) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int shiftId = shift.shiftId();
                if (shiftId == ShiftEntity.NO_ID) {
                    // הכנסה חדשה (Insert)
                    String sql = "INSERT INTO Shifts(branchId, year, week, startDate, day, shiftType) VALUES(?,?,?,?,?,?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        pstmt.setInt(1, branchWeekKey.branchId());
                        pstmt.setInt(2, branchWeekKey.year());
                        pstmt.setInt(3, branchWeekKey.week());
                        pstmt.setString(4, shift.startDate().toString());
                        pstmt.setString(5, shiftEntityKey.day());
                        pstmt.setString(6, shiftEntityKey.type());
                        pstmt.executeUpdate();
                        try (ResultSet rs = pstmt.getGeneratedKeys()) {
                            if (rs.next()) shiftId = rs.getInt(1);
                        }
                    }
                } else {
                    // עדכון (Update)
                    String sql = "UPDATE Shifts SET startDate=?, day=?, shiftType=? WHERE shiftId=?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, shift.startDate().toString());
                        pstmt.setString(2, shiftEntityKey.day());
                        pstmt.setString(3, shiftEntityKey.type());
                        pstmt.setInt(4, shiftId);
                        pstmt.executeUpdate();
                    }
                }

                saveShiftMaps(conn, shiftId, shift.employees(), shift.additionalHours());
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) { throw new RuntimeException("Error saving shift", e); }
    }

    @Override
    public void removeShift(int branchId, int year, int week, String day, String type) {
        String sql = "DELETE FROM Shifts WHERE branchId=? AND year=? AND week=? AND day=? AND shiftType=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, branchId);
            pstmt.setInt(2, year);
            pstmt.setInt(3, week);
            pstmt.setString(4, day);
            pstmt.setString(5, type);
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error removing shift", e); }
    }

    @Override
    public ShiftEntity getShiftById(int shiftId) {
        String sql = "SELECT * FROM Shifts WHERE shiftId=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, shiftId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Map<String, Set<String>> employees = new HashMap<>();
                Map<String, Float> additionalHours = new HashMap<>();

                try (PreparedStatement mapStmt = conn.prepareStatement("SELECT mapKey, mapValue FROM ShiftEmployees WHERE shiftId=?")) {
                    mapStmt.setInt(1, shiftId);
                    ResultSet mapRs = mapStmt.executeQuery();
                    while (mapRs.next()) {
                        employees.computeIfAbsent(mapRs.getString("mapKey"), k -> new HashSet<>()).add(mapRs.getString("mapValue"));
                    }
                }

                try (PreparedStatement hrsStmt = conn.prepareStatement("SELECT employeeId, hours FROM ShiftAdditionalHours WHERE shiftId=?")) {
                    hrsStmt.setInt(1, shiftId);
                    ResultSet hrsRs = hrsStmt.executeQuery();
                    while (hrsRs.next()) {
                        additionalHours.put(hrsRs.getString("employeeId"), hrsRs.getFloat("hours"));
                    }
                }

                return new ShiftEntity(
                        rs.getInt("shiftId"),
                        LocalDateTime.parse(rs.getString("startDate")),
                        rs.getString("day"),
                        rs.getString("shiftType"),
                        employees,
                        additionalHours
                );
            }
        } catch (SQLException e) { throw new RuntimeException("Error loading shift by ID", e); }
        return null;
    }

    private void saveShiftMaps(Connection conn, int shiftId, Map<String, Set<String>> employees, Map<String, Float> additionalHours) throws SQLException {
        try (PreparedStatement delEmp = conn.prepareStatement("DELETE FROM ShiftEmployees WHERE shiftId=?");
             PreparedStatement delHrs = conn.prepareStatement("DELETE FROM ShiftAdditionalHours WHERE shiftId=?")) {
            delEmp.setInt(1, shiftId); delEmp.executeUpdate();
            delHrs.setInt(1, shiftId); delHrs.executeUpdate();
        }

        try (PreparedStatement insEmp = conn.prepareStatement("INSERT INTO ShiftEmployees(shiftId, mapKey, mapValue) VALUES(?,?,?)")) {
            for (Map.Entry<String, Set<String>> entry : employees.entrySet()) {
                for (String val : entry.getValue()) {
                    insEmp.setInt(1, shiftId); insEmp.setString(2, entry.getKey()); insEmp.setString(3, val); insEmp.addBatch();
                }
            }
            insEmp.executeBatch();
        }

        try (PreparedStatement insHrs = conn.prepareStatement("INSERT INTO ShiftAdditionalHours(shiftId, employeeId, hours) VALUES(?,?,?)")) {
            for (Map.Entry<String, Float> entry : additionalHours.entrySet()) {
                insHrs.setInt(1, shiftId); insHrs.setString(2, entry.getKey()); insHrs.setFloat(3, entry.getValue()); insHrs.addBatch();
            }
            insHrs.executeBatch();
        }
    }
}