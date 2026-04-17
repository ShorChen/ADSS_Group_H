package presentation.control;

import domain.entities.Employee;
import domain.entities.Role;
import domain.services.EmployeeService;
import presentation.model.EmployeePL;

/* requirement no. 1 */
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController() {
        this.service = new EmployeeService();
    }


    public String addEmployee(EmployeePL employee) {
        return service.addEmployee(employee.toEmployee());
    }

    public void updateEmployee(EmployeePL employee, String password) {
        service.updateEmployee(employee.toEmployee(), password);
    }

    public boolean deactivateEmployee(String id) {
        System.out.println("Method not yet implemented");
        //Todo: implement
        return false;
    }

    public EmployeePL getEmployeeDetails(String id) {
        if (id == null)
            return null;
        //Todo: implement
        System.out.println("Method not yet implemented");


        return null;
    }

    public boolean grantQualifications(Employee employee, Role... roles) {
        //Todo: implement
        System.out.println("Method not yet implemented");
        return false;
    }

    public boolean revokeQualifications(Employee employee, Role... roles) {
        //Todo: implement
        System.out.println("Method not yet implemented");
        return false;
    }

}
