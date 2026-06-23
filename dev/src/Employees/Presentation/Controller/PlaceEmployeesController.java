package Employees.Presentation.Controller;

import Employees.Context.SessionManager;
import Employees.Domain.DTO.RoleSL;
import Employees.Domain.DTO.ShiftKey;
import Employees.Domain.DTO.ShiftSL;
import Employees.Service.BranchService;
import Employees.Service.EmployeeService;
import Employees.Service.RoleService;
import Employees.Service.ShiftService;
import Employees.Presentation.DTO.EmployeePL;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;
import Employees.Shared.WeekConstants;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlaceEmployeesController {
    private final RoleService roleService;
    private final EmployeeService employeeService;
    private final ShiftService shiftService;

    public PlaceEmployeesController() {
        roleService = new RoleService();
        employeeService = new EmployeeService();
        shiftService = new ShiftService();
    }

    public List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        roleService.getAllRoles().forEach(r -> roles.add(r.getTag()));
        return roles;
    }

    public List<EmployeePL> getAvailableEmployees(WeekDay day, ShiftType type, String role) {
        List<EmployeePL> employeePLList = new ArrayList<>();
        employeeService.getAvailableEmployees(day, type,
                new RoleSL(role)).forEach(e -> employeePLList.add(new EmployeePL(e)));
        return employeePLList;
    }

    public void assignToShift(WeekDay day, ShiftType type, EmployeePL selectedEmployee, String selectedRole, boolean isFirstWeek) {
        int weekOffset = isFirstWeek? 0 : 1;
        LocalDate dateNextWeek = SessionManager.now().plusWeeks(weekOffset).toLocalDate();
        ShiftSL s = shiftService.getShiftsOfWeek(SessionManager.getCurrentEmployee().getBranchId(),
                        dateNextWeek.get(WeekConstants.WEEK_FIELDS.weekBasedYear()),
                        dateNextWeek.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear()))
                .get(new ShiftKey(day, type));

        if (s.doesEmployeeWork(selectedEmployee.getId()))
            throw new UnsupportedOperationException("Employee is already in the shift");

        s.assignEmployeeToRole(new RoleSL(selectedRole), selectedEmployee.toEmployee());

        LocalDate targetDate = SessionManager.now().toLocalDate();

        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getSelectedBranchId();

        shiftService.addUpdateShift(branchId, year, week, day.toString(), type.toString(), s);
    }

    public boolean isFirstWeek() {
        LocalDate targetDate = SessionManager.now().toLocalDate();

        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getSelectedBranchId();

        return new BranchService().isFirstWeek(branchId, year, week);
    }
}
