package presentation.control;

import domain.entities.Employee;
import domain.entities.Role;
import domain.services.EmployeeService;

/* requirement no. 1 */
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    public boolean addEmployee(Employee employee) {
        //Todo: implement
        System.out.println("Method not yet implemented");

        return false;
    }
    public boolean deactivateEmployee(String id){
        System.out.println("Method not yet implemented");
        //Todo: implement
        return false;
    }
    public Employee getEmployeeDetails(String id){
        //Todo: implement
        System.out.println("Method not yet implemented");

        return null;
    }
    public boolean updateEmployee(String id, Employee employee){
        //Todo: implement
        System.out.println("Method not yet implemented");

        return false;
    }
    public boolean grantQualifications(Employee employee, Role... roles){
        //Todo: implement
        System.out.println("Method not yet implemented");
        return false;
    }
    public boolean revokeQualifications(Employee employee, Role... roles){
        //Todo: implement
        System.out.println("Method not yet implemented");
        return false;
    }
}
