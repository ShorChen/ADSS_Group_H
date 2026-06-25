package Employees.DataAccess.SqlImpl;

import Core.DataAccess.DatabaseManager;
import Employees.DataAccess.EmployeeDAO;
import Employees.Domain.Entities.EmployeeDL;
import Employees.Domain.Entities.AvailabilitySubmissionDL;
import Employees.Domain.Entities.RoleDL;
import Employees.Shared.Enums.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlEmployeeDAO implements EmployeeDAO {

    @Override
    public void addUpdateEmployee(EmployeeDL employee) {
        String sql = "INSERT OR REPLACE INTO Employees(employeeId, name, bankAccount, salary, salaryType, dateOfEmployment, jobScope, constraints, yearlyRestDays, weeklyRestDay, workingDoubles, active, branchId) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, employee.getId());
                pstmt.setString(2, employee.getName());
                pstmt.setString(3, employee.getBankAccount());
                pstmt.setDouble(4, employee.getSalary());
                pstmt.setString(5, employee.getSalaryType().name());
                pstmt.setString(6, employee.getDateOfEmployment().toString());
                pstmt.setString(7, employee.getJobScope().name());
                pstmt.setString(8, employee.getConstraints());
                pstmt.setInt(9, employee.getYearlyRestDays());
                pstmt.setString(10, employee.getWeeklyRestDay().name());

                boolean worksDoubles = employee.getAvailabilitySubmission() != null && employee.getAvailabilitySubmission().isWorkingDoubles();
                pstmt.setInt(11, worksDoubles ? 1 : 0);
                pstmt.setInt(12, employee.isActive() ? 1 : 0);
                pstmt.setInt(13, employee.getBranchId());
                pstmt.executeUpdate();

                // 1. Save Roles (FIXED: Iterating as RoleDL, saving string tag)
                try (PreparedStatement delRoles = conn.prepareStatement("DELETE FROM EmployeeRoles WHERE employeeId=?")) {
                    delRoles.setString(1, employee.getId());
                    delRoles.executeUpdate();
                }
                if (employee.getQualifiedRoles() != null && !employee.getQualifiedRoles().isEmpty()) {
                    try (PreparedStatement insRole = conn.prepareStatement("INSERT INTO EmployeeRoles(employeeId, roleName) VALUES(?,?)")) {
                        for (RoleDL role : employee.getQualifiedRoles()) {
                            insRole.setString(1, employee.getId());
                            insRole.setString(2, role.getTag());
                            insRole.addBatch();
                        }
                        insRole.executeBatch();
                    }
                }

                // 2. Save Availability
                try (PreparedStatement delShifts = conn.prepareStatement("DELETE FROM EmployeeAvailability WHERE employeeId=?")) {
                    delShifts.setString(1, employee.getId());
                    delShifts.executeUpdate();
                }
                if (employee.getAvailabilitySubmission() != null && employee.getAvailabilitySubmission().getShifts() != null) {
                    try (PreparedStatement insShift = conn.prepareStatement("INSERT INTO EmployeeAvailability(employeeId, day, shiftType, isAvailable) VALUES(?,?,?,?)")) {
                        for (Map.Entry<String, Boolean> entry : employee.getAvailabilitySubmission().getShifts().entrySet()) {
                            String[] parts = entry.getKey().split("_");
                            insShift.setString(1, employee.getId());
                            insShift.setString(2, parts[0]); // Day
                            insShift.setString(3, parts[1]); // Type
                            insShift.setInt(4, Boolean.TRUE.equals(entry.getValue()) ? 1 : 0);
                            insShift.addBatch();
                        }
                        insShift.executeBatch();
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving employee to DB", e);
        }
    }

    @Override
    public EmployeeDL getEmployee(String id) {
        String sql = "SELECT * FROM Employees WHERE employeeId=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                List<RoleDL> roles = new ArrayList<>();
                try (PreparedStatement pstmtRoles = conn.prepareStatement("SELECT roleName FROM EmployeeRoles WHERE employeeId=?")) {
                    pstmtRoles.setString(1, id);
                    ResultSet rsRoles = pstmtRoles.executeQuery();
                    while (rsRoles.next()) roles.add(new RoleDL(rsRoles.getString("roleName")));
                }

                Map<String, Boolean> shifts = new HashMap<>();
                try (PreparedStatement pstmtShifts = conn.prepareStatement("SELECT day, shiftType, isAvailable FROM EmployeeAvailability WHERE employeeId=?")) {
                    pstmtShifts.setString(1, id);
                    ResultSet rsShifts = pstmtShifts.executeQuery();
                    while (rsShifts.next()) {
                        String key = rsShifts.getString("day") + "_" + rsShifts.getString("shiftType");
                        shifts.put(key, rsShifts.getInt("isAvailable") == 1);
                    }
                }

                AvailabilitySubmissionDL availability = new AvailabilitySubmissionDL(
                        id, shifts, rs.getInt("workingDoubles") == 1
                );

                return new EmployeeDL(
                        rs.getString("employeeId"),
                        rs.getString("name"),
                        rs.getString("bankAccount"),
                        rs.getDouble("salary"),
                        SalaryType.valueOf(rs.getString("salaryType")),
                        LocalDateTime.parse(rs.getString("dateOfEmployment")),
                        JobScope.valueOf(rs.getString("jobScope")),
                        roles,
                        rs.getString("constraints"),
                        rs.getInt("yearlyRestDays"),
                        WeekDay.valueOf(rs.getString("weeklyRestDay")),
                        availability,
                        rs.getInt("active") == 1,
                        rs.getInt("branchId")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading employee", e);
        }
        return null;
    }

    @Override
    public List<EmployeeDL> getAllEmployees() {
        List<EmployeeDL> list = new ArrayList<>();
        String sql = "SELECT employeeId FROM Employees";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(getEmployee(rs.getString("employeeId")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all employees", e);
        }
        return list;
    }
}