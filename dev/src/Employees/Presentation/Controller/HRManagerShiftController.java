package Employees.Presentation.Controller;

import Employees.Context.SessionManager;
import Employees.Domain.DTO.EmployeeSL;
import Employees.Domain.DTO.RoleSL;
import Employees.Domain.DTO.ShiftKey;
import Employees.Domain.DTO.ShiftSL;
import Employees.Presentation.DTO.EmployeePL;
import Employees.Service.*;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;
import Employees.Shared.WeekConstants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HRManagerShiftController {
    private final ShiftService service;
    private final EmployeeService employeeService;
    private final StoreDetailsService storeService;
    private final RoleService roleService;
    private final BranchService branchService;


    public HRManagerShiftController() {
        this.service = new ShiftService();
        this.employeeService = new EmployeeService();
        storeService = new StoreDetailsService();
        roleService = new RoleService();
        branchService = new BranchService();
    }

    public void openShift(WeekDay day, ShiftType type, EmployeePL shiftManager, boolean isFirstWeek) {
        int weekOffset = isFirstWeek? 0 : 1;
        LocalDate targetDate = SessionManager.now().plusWeeks(weekOffset).toLocalDate();
        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getSelectedBranchId();
        service.addUpdateShift(branchId, year, week, day.toString(), type.toString(),
                new ShiftSL(day, type, shiftManager.getId()));
    }

    public void closeShift(WeekDay day, ShiftType type, boolean isFirstWeek) {
        int weekOffset = isFirstWeek? 0 : 1;
        LocalDate targetDate = SessionManager.now().plusWeeks(weekOffset).toLocalDate();
        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getSelectedBranchId();

        service.closeShift(branchId, year, week, day.toString(), type.toString());
    }

    public EmployeePL getShiftManager(String id) {
        EmployeeSL employee = employeeService.getEmployeeDetails(id);
        if (employee == null) throw new IllegalArgumentException("Employee does not exist");
        if (!employee.getQualifiedRoles().contains(RoleSL.ShiftManager))
            throw new IllegalArgumentException("Employee is not a shift manager");
        if(employee.getBranchId() != SessionManager.getSelectedBranchId())
            throw  new IllegalArgumentException("Employee does not work in this branch");
        return new EmployeePL(employee);
    }

    public void setShiftStaffing(WeekDay day, ShiftType type, Map<String, Integer> staffing, boolean isFirstWeek) {
        int weekOffset = isFirstWeek? 0 : 1;
        LocalDate targetDate = SessionManager.now().plusWeeks(weekOffset).toLocalDate();
        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getSelectedBranchId();

        ShiftSL shift = service.getShiftsOfWeek(branchId, year, week).get(new ShiftKey(day, type));
        Map<RoleSL, Integer> capacities = new HashMap<>();
        staffing.forEach((role, amount) -> capacities.put(new RoleSL(role), amount));
        shift.setCapacities(capacities);
        service.addUpdateShift(branchId, year, week, day.toString(), type.toString(), shift);
    }

    public boolean isFirstWeek() {
        LocalDate targetDate = SessionManager.now().toLocalDate();

        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getSelectedBranchId();

        return branchService.isFirstWeek(branchId, year, week);
    }

    public List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        roleService.getAllRoles().forEach(r -> roles.add(r.getTag()));
        return roles;
    }

}
