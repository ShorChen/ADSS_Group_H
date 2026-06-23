package Employees.DataAccess.SqlImpl;

import Core.DataAccess.DatabaseManager;
import Employees.DataAccess.RequestDAO;
import Employees.DataAccess.ShiftDAO;
import Employees.DataAccess.Entities.RequestEntity;
import Employees.DataAccess.Entities.ShiftEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SqlRequestDAO implements RequestDAO {

    private final ShiftDAO shiftDAO = new SqlShiftDAO();

    @Override
    public void addUpdateRequest(RequestEntity request) {
        String sql = "INSERT OR REPLACE INTO Requests(requestId, shiftId, prevEmployee, newEmployee, manager, prevApproved, newApproved, managerApproved, denied) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // אם זו בקשה חדשה (נניח ש-0 זה ללא ID), נוכל להשתמש ב-INSERT רגיל, אבל REPLACE יטפל בזה.
            if(request.requestId() > 0) pstmt.setInt(1, request.requestId()); else pstmt.setNull(1, Types.INTEGER);
            pstmt.setInt(2, request.shift().shiftId());
            pstmt.setString(3, request.prevEmployee());
            pstmt.setString(4, request.newEmployee());
            pstmt.setString(5, request.manager());
            pstmt.setString(6, request.prevApproved());
            pstmt.setString(7, request.newApproved());
            pstmt.setString(8, request.managerApproved());
            pstmt.setInt(9, request.denied() ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error saving request", e); }
    }

    @Override
    public boolean exists(RequestEntity request) {
        String sql = "SELECT 1 FROM Requests WHERE requestId=? OR (shiftId=? AND prevEmployee=? AND newEmployee=?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, request.requestId());
            pstmt.setInt(2, request.shift().shiftId());
            pstmt.setString(3, request.prevEmployee());
            pstmt.setString(4, request.newEmployee());
            return pstmt.executeQuery().next();
        } catch (SQLException e) { throw new RuntimeException("Error checking request", e); }
    }

    @Override
    public List<RequestEntity> getPendingRequests(String id) {
        List<RequestEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM Requests WHERE prevEmployee=? OR newEmployee=? OR manager=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id); pstmt.setString(2, id); pstmt.setString(3, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) list.add(mapResultSetToEntity(rs));
        } catch (SQLException e) { throw new RuntimeException("Error loading pending requests", e); }
        return list;
    }

    @Override
    public List<RequestEntity> getAll() {
        List<RequestEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM Requests";
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapResultSetToEntity(rs));
        } catch (SQLException e) { throw new RuntimeException("Error loading all requests", e); }
        return list;
    }

    private RequestEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        int shiftId = rs.getInt("shiftId");
        ShiftEntity shift = shiftDAO.getShiftById(shiftId);

        return new RequestEntity(
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