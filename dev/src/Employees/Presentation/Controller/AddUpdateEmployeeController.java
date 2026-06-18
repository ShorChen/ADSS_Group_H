package Employees.Presentation.Controller;

import Employees.Domain.Service.StoreDetailsService;
import Employees.Presentation.Model.EmployeePL;
import Employees.Shared.Enums.WeekDay;

import java.util.List;

public class AddUpdateEmployeeController {
    private final EmployeeController employeeController;
    private final RoleController roleController;
    private final StoreDetailsService storeDetailsService;

    public AddUpdateEmployeeController() {
        employeeController = new EmployeeController();
        roleController = new RoleController();
        storeDetailsService = new StoreDetailsService();
    }

    public List<WeekDay> getClosedDays() {
        return storeDetailsService.getClosedDays();
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

    public List<String> getAllRoles() {
        return roleController.getAllRoles();
    }
}
