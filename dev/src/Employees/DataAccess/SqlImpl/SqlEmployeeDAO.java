package Employees.DataAccess.SqlImpl;

import Core.DataAccess.DatabaseManager;
import Employees.DataAccess.EmployeeDAO;
import Employees.DataAccess.Entities.EmployeeEntity;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class SqlEmployeeDAO implements EmployeeDAO {

    @Override
    public void addUpdateEmployee(EmployeeEntity employee) {
        String sql = "INSERT OR REPLACE INTO Employees(employeeId, name, bankAccount, salary, salaryType, dateOfEmployment, jobScope, constraints, yearlyRestDays, weeklyRestDay, password, workingDoubles, active, branchId) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, employee.id());
                pstmt.setString(2, employee.name());
                pstmt.setString(3, employee.bankAccount());
                pstmt.setDouble(4, employee.salary());
                pstmt.setString(5, employee.salaryType());
                pstmt.setString(6, employee.dateOfEmployment().toString());
                pstmt.setString(7, employee.jobScope());
                pstmt.setString(8, employee.constraints());
                pstmt.setInt(9, employee.yearlyRestDays());
                pstmt.setString(10, employee.weeklyRestDay());
                pstmt.setString(11, employee.password());
                pstmt.setInt(12, employee.workingDoubles() ? 1 : 0);
                pstmt.setInt(13, employee.active() ? 1 : 0);
                pstmt.setInt(14, employee.branchId());
                pstmt.executeUpdate();

                try (PreparedStatement delRoles = conn.prepareStatement("DELETE FROM EmployeeRoles WHERE employeeId=?")) {
                    delRoles.setString(1, employee.id());
                    delRoles.executeUpdate();
                }
                if (employee.qualifiedRoles() != null && !employee.qualifiedRoles().isEmpty()) {
                    try (PreparedStatement insRole = conn.prepareStatement("INSERT INTO EmployeeRoles(employeeId, roleName) VALUES(?,?)")) {
                        for (String role : employee.qualifiedRoles()) {
                            insRole.setString(1, employee.id());
                            insRole.setString(2, role);
                            insRole.addBatch();
                        }
                        insRole.executeBatch();
                    }
                }

                try (PreparedStatement delShifts = conn.prepareStatement("DELETE FROM EmployeeUnavailableShifts WHERE employeeId=?")) {
                    delShifts.setString(1, employee.id());
                    delShifts.executeUpdate();
                }
                if (employee.unavailableShifts() != null && !employee.unavailableShifts().isEmpty()) {
                    try (PreparedStatement insShift = conn.prepareStatement("INSERT INTO EmployeeUnavailableShifts(employeeId, weekDay, shiftType) VALUES(?,?,?)")) {
                        for (Map.Entry<Integer, Set<Integer>> entry : employee.unavailableShifts().entrySet()) {
                            for (Integer shiftType : entry.getValue()) {
                                insShift.setString(1, employee.id());
                                insShift.setInt(2, entry.getKey());
                                insShift.setInt(3, shiftType);
                                insShift.addBatch();
                            }
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
    public EmployeeEntity getEmployee(String id) {
        String sql = "SELECT * FROM Employees WHERE employeeId=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                List<String> roles = new ArrayList<>();
                try (PreparedStatement pstmtRoles = conn.prepareStatement("SELECT roleName FROM EmployeeRoles WHERE employeeId=?")) {
                    pstmtRoles.setString(1, id);
                    ResultSet rsRoles = pstmtRoles.executeQuery();
                    while (rsRoles.next()) roles.add(rsRoles.getString("roleName"));
                }

                Map<Integer, Set<Integer>> shifts = new HashMap<>();
                try (PreparedStatement pstmtShifts = conn.prepareStatement("SELECT weekDay, shiftType FROM EmployeeUnavailableShifts WHERE employeeId=?")) {
                    pstmtShifts.setString(1, id);
                    ResultSet rsShifts = pstmtShifts.executeQuery();
                    while (rsShifts.next()) {
                        int day = rsShifts.getInt("weekDay");
                        int type = rsShifts.getInt("shiftType");
                        shifts.putIfAbsent(day, new HashSet<>());
                        shifts.get(day).add(type);
                    }
                }

                return new EmployeeEntity(
                        rs.getString("employeeId"),
                        rs.getString("name"),
                        rs.getString("bankAccount"),
                        rs.getDouble("salary"),
                        rs.getString("salaryType"),
                        LocalDateTime.parse(rs.getString("dateOfEmployment")),
                        rs.getString("jobScope"),
                        roles,
                        rs.getString("constraints"),
                        rs.getInt("yearlyRestDays"),
                        rs.getString("weeklyRestDay"),
                        rs.getString("password"),
                        rs.getInt("workingDoubles") == 1,
                        shifts,
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
    public boolean exists(String id) {
        return getEmployee(id) != null;
    }

    @Override
    public boolean updatePassword(String id, String oldPass, String newPass) {
        EmployeeEntity employee = getEmployee(id);
        if (employee == null || !employee.checkPassword(oldPass)) return false;

        addUpdateEmployee(employee.changePassword(newPass));
        return true;
    }

    @Override
    public void deactivateEmployee(String id) {
        EmployeeEntity employee = getEmployee(id);
        if (employee == null) throw new IllegalArgumentException("No employee found");

        addUpdateEmployee(employee.changeActivityStatus(false));
    }

    @Override
    public List<EmployeeEntity> getEmployeesWithRole(String roleName) {
        List<EmployeeEntity> list = new ArrayList<>();
        String sql = "SELECT employeeId FROM EmployeeRoles WHERE roleName=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roleName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                EmployeeEntity emp = getEmployee(rs.getString("employeeId"));
                if (emp != null) {
                    list.add(emp);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading employees by role", e);
        }
        return list;
    }
}