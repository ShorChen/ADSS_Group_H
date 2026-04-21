package presentation.ui_manager;

import context.SessionManager;
import domain.entities.Employee;
import domain.entities.Role;
import domain.enums.JobScope;
import domain.enums.SalaryType;
import domain.enums.WeekDay;
import domain.services.EmployeeService;
import domain.services.ShiftService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CreateStoreController {
    private final EmployeeService employeeService;
    private final ShiftService shiftService;

    public CreateStoreController() {
        employeeService = new EmployeeService();
        shiftService = new ShiftService();
    }

    public String registerManager(String id, String name, String bankAccount, String weekDay) {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.MANAGER);
        return employeeService.addEmployee(new Employee(
                id, name, bankAccount, 0, SalaryType.GLOBALLY,
                SessionManager.now(), JobScope.FULL_TIME,
                roles, "", 24, WeekDay.valueOf(weekDay),
                false, new HashMap<>(), true
        ));
    }

    public void setClosedDays(List<WeekDay> closeDays) {
        List<String> closed = new ArrayList<>();
        closeDays.forEach(w -> closed.add(w.name()));
        shiftService.setClosedDays(closed);
    }
}
