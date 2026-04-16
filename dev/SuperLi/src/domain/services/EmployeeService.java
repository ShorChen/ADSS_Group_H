package domain.services;

import domain.entities.Employee;
import domain.entities.Role;
import data_access.pools.EmployeePool;
import domain.entities.Employee;

/* requirement no. 1 */
public class EmployeeService {
    private final EmployeePool employeePool;

    public EmployeeService(EmployeePool employeePool) {
        this.employeePool = employeePool;
    }

    public boolean addEmployee(Employee employee) {
        if (employeePool.exists(employee.getId())){
            return false;
        }
        employeePool.addEmployee(employee);
        return true;
    }

    public Employee getEmployeeDetails(String id){
       return employeePool.getEmployee(id);
    }
    
    public boolean deactivateEmployee(String id){
        return employeePool.removeEmpoyee(id);
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
