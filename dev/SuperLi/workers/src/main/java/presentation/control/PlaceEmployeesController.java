package presentation.control;

import context.SessionManager;
import domain.entities.Role;
import domain.entities.Shift;
import domain.entities.WeekShifts;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import domain.services.EmployeeService;
import domain.services.RoleService;
import domain.services.ShiftService;
import presentation.model.EmployeePL;

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

    public List<EmployeePL> getAvailableEmployees(int day, int type, String role) {
        List<EmployeePL> employeePLList = new ArrayList<>();
        employeeService.getAvailableEmployees(WeekDay.values()[day], ShiftType.values()[type],
                new Role(role)).forEach(e -> employeePLList.add(new EmployeePL(e)));
        return employeePLList;
    }

    public void assignToShift(int day, int type, EmployeePL employeePL, String role) {
        WeekShifts week = shiftService.getNWeeksAgo(SessionManager.now().toLocalDate());
        ShiftType shiftType = ShiftType.values()[type];

        Shift s = week.getDayShifts().get(WeekDay.values()[day]);
        if (shiftType == ShiftType.EVENING)
            s = week.getNightShifts().get(WeekDay.values()[day]);

        s.assign(new Role(role), employeePL.toEmployee());
        shiftService.updateWeek(week);

    }
}
