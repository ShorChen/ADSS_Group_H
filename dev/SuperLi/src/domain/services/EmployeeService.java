package domain.services;

import data_access.pools.EmployeePool;
import domain.entities.Employee;
import domain.util.PasswordGenerator;

public class EmployeeService {

    private final EmployeePool employees;

    public EmployeeService() {
        this.employees = EmployeePool.Instance();
    }

    public String addEmployee(Employee employee) {
        String pass = PasswordGenerator.generatePassword();
        if (!employees.containsEmployee(employee.getId())) {
            employees.addEmployee(employee.toEntity(pass));
            return pass;
        }
        throw new IllegalArgumentException("An employee with the given id is already present in the system");
    }

    public boolean deactivateEmployee(String id) {
        //Todo: implement
        return false;
    }

    public Employee getEmployeeDetails(String id) {
        //Todo: implement
        return null;
    }

    public void updateEmployee(Employee employee, String password) {
        employees.getEmployee(employee.getId()).update(employee.toEntity(password));
    }
}
