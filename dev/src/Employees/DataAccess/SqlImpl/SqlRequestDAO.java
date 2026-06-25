package Employees.DataAccess.SqlImpl;

import Core.DataAccess.DatabaseManager;
import Employees.DataAccess.RequestDAO;
import Employees.Domain.Entities.RequestDL;
import Employees.Domain.Entities.ShiftDL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlRequestDAO implements RequestDAO {

    private final SqlShiftDAO shiftDAO = new SqlShiftDAO();

    @Override
    public void addUpdateRequest(RequestDL request) {
        String sql = "INSERT OR REPLACE INTO Requests(requestId, shiftId, prevEmployee, newEmployee, manager, prevApproved, newApproved, managerApproved, denied) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (request.getRequestId() > 0) pstmt.setInt(1, request.getRequestId());
            else pstmt.setNull(1, Types.INTEGER);

            pstmt.setInt(2, request.getShift().getShiftId());
            pstmt.setString(3, request.getPrevEmployeeId());
            pstmt.setString(4, request.getNewEmployeeId());
            pstmt.setString(5, request.getManagerId());
            pstmt.setString(6, request.getPrevApproved());
            pstmt.setString(7, request.getNewApproved());
            pstmt.setString(8, request.getManagerApproved());
            pstmt.setInt(9, request.isDenied() ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving request", e);
        }
    }

    @Override
    public List<RequestDL> getAll() {
        List<RequestDL> list = new ArrayList<>();
        String sql = "SELECT * FROM Requests";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapResultSetToEntity(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading all requests", e);
        }
        return list;
    }

    private RequestDL mapResultSetToEntity(ResultSet rs) throws SQLException {
        int shiftId = rs.getInt("shiftId");
        ShiftDL shift = shiftDAO.getShiftById(shiftId);

        return new RequestDL(
                rs.getInt("requestId"),
                shift,
                rs.getString("prevEmployee"),
                rs.getString("newEmployee"),
                rs.getString("manager"),
                rs.getString("prevApproved"),
                rs.getString("newApproved"),
                rs.getString("managerApproved"),
                rs.getInt("denied") == 1
        );
    }
}