package Employees.Domain.Facades;

import Employees.DataAccess.SqlImpl.*;
import Employees.Domain.Entities.*;
import java.util.List;

public class EmployeesFacade {
    private final SqlEmployeeDAO employeeDAO = new SqlEmployeeDAO();
    private final SqlShiftDAO shiftDAO = new SqlShiftDAO();
    private final SqlRoleDAO roleDAO = new SqlRoleDAO();
    private final SqlBranchDAO branchDAO = new SqlBranchDAO();
    private final SqlRequestDAO requestDAO = new SqlRequestDAO();
    private final SqlStoreDAO storeDAO = new SqlStoreDAO();

    // --- EMPLOYEES ---
    public void addUpdateEmployee(EmployeeDL emp) { employeeDAO.addUpdateEmployee(emp); }
    public EmployeeDL getEmployee(String id) { return employeeDAO.getEmployee(id); }
    public List<EmployeeDL> getAllEmployees() { return employeeDAO.getAllEmployees(); }

    // --- SHIFTS ---
    public void addUpdateShift(ShiftDL shift) { shiftDAO.addUpdateShift(shift); }
    public ShiftDL getShift(int id) { return shiftDAO.getShiftById(id); }
    public List<ShiftDL> getShiftsByBranchAndWeek(int branchId, int year, int week) {
        return shiftDAO.getShiftsByBranchAndWeek(branchId, year, week);
    }
    public List<ShiftDL> getShiftsByBranch(int branchId) { return shiftDAO.getShiftsByBranch(branchId); }
    public void saveDeadline(int branchId, int year, int week, String deadline) {
        shiftDAO.saveDeadline(branchId, year, week, deadline);
    }
    public String getDeadline(int branchId, int year, int week) {
        return shiftDAO.getDeadline(branchId, year, week);
    }

    // --- ROLES & STORE ---
    public List<String> getAllRoles() { return roleDAO.getAllRoles(); }
    public void addRole(String role) { roleDAO.addRole(role); }
    public StoreDetailsDL getStoreDetails() { return storeDAO.getStoreDetails(); }
    public void updateStoreDetails(StoreDetailsDL details) { storeDAO.updateStoreDetails(details); }

    // --- BRANCHES & REQUESTS ---
    public List<BranchDL> getAllBranches() { return branchDAO.getAllBranches(); }
    public void addUpdateBranch(BranchDL branch) { branchDAO.addUpdateBranch(branch); }
    public void addUpdateRequest(RequestDL request) { requestDAO.addUpdateRequest(request); }
    public List<RequestDL> getAllRequests() { return requestDAO.getAll(); }
}