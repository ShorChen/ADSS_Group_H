package Employees.DataAccess.SqlImpl;

import Core.DataAccess.DatabaseManager;
import Employees.DataAccess.ShiftDAO;
import Employees.Domain.Entities.ShiftDL;
import Employees.Domain.Entities.RoleDL;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class SqlShiftDAO implements ShiftDAO {

    @Override
    public List<ShiftDL> getShiftsByBranchAndWeek(int branchId, int year, int week) {
        List<ShiftDL> shifts = new ArrayList<>();
        String sql = "SELECT shiftId FROM Shifts WHERE branchId=? AND year=? AND week=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
    public void addUpdateShift(ShiftDL shift) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int shiftId = shift.getShiftId();
                if (shiftId == 0) {
                    String sql = "INSERT INTO Shifts(branchId, year, week, startDate, day, shiftType) VALUES(?,?,?,?,?,?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        pstmt.setInt(1, shift.getBranchId());
                        pstmt.setInt(2, shift.getYear());
                        pstmt.setInt(3, shift.getWeek());
                        pstmt.setString(4, shift.getStartDate().toString());
                        pstmt.setString(5, shift.getDay().name());
                        pstmt.setString(6, shift.getShiftType().name());
                        pstmt.executeUpdate();
                        try (ResultSet rs = pstmt.getGeneratedKeys()) {
                            if (rs.next()) shiftId = rs.getInt(1);
                        }
                    }
                } else {
                    String sql = "UPDATE Shifts SET startDate=?, day=?, shiftType=? WHERE shiftId=?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, shift.getStartDate().toString());
                        pstmt.setString(2, shift.getDay().name());
                        pstmt.setString(3, shift.getShiftType().name());
                        pstmt.setInt(4, shiftId);
                        pstmt.executeUpdate();
                    }
                }

                saveShiftMaps(conn, shiftId, shift.getEmployees(), shift.getAdditionalHours());
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) { throw new RuntimeException("Error saving shift", e); }
    }

    @Override
    public ShiftDL getShiftById(int shiftId) {
        String sql = "SELECT * FROM Shifts WHERE shiftId=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, shiftId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Map<RoleDL, Set<String>> employees = new HashMap<>();
                Map<String, Float> additionalHours = new HashMap<>();

                try (PreparedStatement mapStmt = conn.prepareStatement("SELECT mapKey, mapValue FROM ShiftEmployees WHERE shiftId=?")) {
                    mapStmt.setInt(1, shiftId);
                    ResultSet mapRs = mapStmt.executeQuery();
                    while (mapRs.next()) {
                        RoleDL role = new RoleDL(mapRs.getString("mapKey"));
                        employees.computeIfAbsent(role, ignored -> new HashSet<>()).add(mapRs.getString("mapValue"));
                    }
                }

                try (PreparedStatement hrsStmt = conn.prepareStatement("SELECT employeeId, hours FROM ShiftAdditionalHours WHERE shiftId=?")) {
                    hrsStmt.setInt(1, shiftId);
                    ResultSet hrsRs = hrsStmt.executeQuery();
                    while (hrsRs.next()) {
                        additionalHours.put(hrsRs.getString("employeeId"), hrsRs.getFloat("hours"));
                    }
                }

                return new ShiftDL(
                        rs.getInt("shiftId"),
                        rs.getInt("branchId"),
                        rs.getInt("year"),
                        rs.getInt("week"),
                        LocalDateTime.parse(rs.getString("startDate")),
                        WeekDay.valueOf(rs.getString("day")),
                        ShiftType.valueOf(rs.getString("shiftType")),
                        employees,
                        additionalHours
                );
            }
        } catch (SQLException e) { throw new RuntimeException("Error loading shift by ID", e); }
        return null;
    }

    private void saveShiftMaps(Connection conn, int shiftId, Map<RoleDL, Set<String>> employees, Map<String, Float> additionalHours) throws SQLException {
        try (PreparedStatement delEmp = conn.prepareStatement("DELETE FROM ShiftEmployees WHERE shiftId=?");
             PreparedStatement delHrs = conn.prepareStatement("DELETE FROM ShiftAdditionalHours WHERE shiftId=?")) {
            delEmp.setInt(1, shiftId); delEmp.executeUpdate();
            delHrs.setInt(1, shiftId); delHrs.executeUpdate();
        }

        try (PreparedStatement insEmp = conn.prepareStatement("INSERT INTO ShiftEmployees(shiftId, mapKey, mapValue) VALUES(?,?,?)")) {
            for (Map.Entry<RoleDL, Set<String>> entry : employees.entrySet()) {
                String roleString = entry.getKey().getTag();
                for (String val : entry.getValue()) {
                    insEmp.setInt(1, shiftId);
                    insEmp.setString(2, roleString);
                    insEmp.setString(3, val);
                    insEmp.addBatch();
                }
            }
            insEmp.executeBatch();
        }

        try (PreparedStatement insHrs = conn.prepareStatement("INSERT INTO ShiftAdditionalHours(shiftId, employeeId, hours) VALUES(?,?,?)")) {
            for (Map.Entry<String, Float> entry : additionalHours.entrySet()) {
                insHrs.setInt(1, shiftId);
                insHrs.setString(2, entry.getKey());
                insHrs.setFloat(3, entry.getValue());
                insHrs.addBatch();
            }
            insHrs.executeBatch();
        }
    }
}