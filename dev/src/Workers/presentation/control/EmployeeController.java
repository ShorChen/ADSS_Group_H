package Workers.presentation.control;

import Workers.domain.entities.Employee;
import Workers.domain.entities.Role;
import Workers.domain.services.EmployeeService;
import Workers.presentation.model.EmployeePL;

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
        return service.deactivateEmployee(id);
    }

    public EmployeePL getEmployeeDetails(String id) {
        if (id == null || id.trim().isEmpty()) return null;
    
        Employee employee = service.getEmployeeDetails(id);
        if (employee != null) {
            return new EmployeePL(employee); 
        }
        return null;
    }

    public boolean grantQualifications(Employee employee, Role... roles) {
        // Todo: implement
        System.out.println("Method not yet implemented");
        return false;
    }

    public boolean revokeQualifications(Employee employee, Role... roles) {
        // Todo: implement
        System.out.println("Method not yet implemented");
        return false;
    }

}
