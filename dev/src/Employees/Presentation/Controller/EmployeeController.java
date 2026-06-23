package Employees.Presentation.Controller;

import Employees.Context.SessionManager;
import Employees.Domain.DTO.EmployeeSL;
import Employees.Domain.DTO.RoleSL;
import Employees.Service.EmployeeService;
import Employees.Presentation.DTO.EmployeePL;

public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController() {
        this.service = new EmployeeService();
    }


    public String addEmployee(EmployeePL employee) {
        return service.addEmployee(employee.toEmployee());
    }

    public boolean updateEmployee(EmployeePL employee, String password) {
        return service.updateEmployee(employee.toEmployee(), password);
    }

    public boolean deactivateEmployee(String id) {
        if (id == null || id.trim().isEmpty()) return false;
        return service.deactivateEmployee(SessionManager.getSelectedBranchId(), id);
    }

    public EmployeePL getEmployeeDetails(String id) {
        if (id == null || id.trim().isEmpty()) return null;
        EmployeeSL employee = service.getEmployeeDetails(id);
        if (employee != null) {
            return new EmployeePL(employee);
        }
        return null;
    }

    public boolean exists(String id) {
        return service.containsEmployee(id);
    }
}
