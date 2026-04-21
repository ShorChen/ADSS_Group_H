package presentation.control;

import domain.entities.Role;
import domain.enums.WeekDay;
import domain.services.ShiftService;
import presentation.model.EmployeePL;

import java.util.List;

public class AddUpdateEmployeeController {
    private final ShiftService shiftService;
    private final EmployeeController employeeController;
    private final RoleController roleController;

    public AddUpdateEmployeeController() {
        shiftService = new ShiftService();
        employeeController = new EmployeeController();
        roleController = new RoleController();
    }

    public List<WeekDay> getClosedDays() {
        return shiftService.getClosedDays();
    }

    public EmployeePL getEmployeeDetails(String employeeId) {
        return employeeController.getEmployeeDetails(employeeId);
    }

    public String addEmployee(EmployeePL employee) {
        return employeeController.addEmployee(employee);
    }

    public boolean updateEmployee(EmployeePL employee, String password) {
        return employeeController.updateEmployee(employee, password);
    }

    public List<Role> getAllRoles() {
        return roleController.getAllRoles();
    }

    public String getRole(String role) {
        return roleController.getRole(role);
    }
}
