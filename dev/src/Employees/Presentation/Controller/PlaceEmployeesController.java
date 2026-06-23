package Employees.Presentation.Controller;

import Employees.Domain.Entities.RoleSL;
import Employees.Service.EmployeeService;
import Employees.Service.RoleService;
import Employees.Service.ShiftService;
import Employees.Presentation.DTO.EmployeePL;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;

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

    public List<EmployeePL> getAvailableEmployees(String day, String type, String role) {
        List<EmployeePL> employeePLList = new ArrayList<>();
        employeeService.getAvailableEmployees(WeekDay.fromArgs(day), ShiftType.fromType(type),
                new RoleSL(role)).forEach(e -> employeePLList.add(new EmployeePL(e)));
        return employeePLList;
    }

    public void assignToShift(String day, String type, EmployeePL selectedEmployee, String selectedRole) {

        // todo
    }

//    public void assignToShift(String day, String type, EmployeePL employeePL, String role) {
//
//        LocalDate dateNextWeek = SessionManager.now().plusWeeks(1).toLocalDate();
//        Shift s = shiftService.getShiftsOfWeek(SessionManager.getCurrentEmployee().getBranchId(),
//                        dateNextWeek.get(WeekConstants.WEEK_FIELDS.weekBasedYear()),
//                        dateNextWeek.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear()))
//                .get(new ShiftKey(WeekDay.fromArgs(day), ShiftType.fromType(type)));
//
//        if (s.doesEmployeeWork(employeePL.getId()))
//            throw new UnsupportedOperationException("Employee is already in the shift");
//
//        s.assignEmployeeToRole(new Role(role), employeePL.toEmployee());
//        shiftService.updateShift(s);
//
//    }
}
