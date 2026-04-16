package presentation.controllers;

import domain.entities.Employee;
import domain.services.EmployeeService;

public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public String addEmployee(String id) {
        Employee employee = new Employee(id);
        boolean success = employeeService.addEmployee(employee);
        
        return success ? "Added a new employee successfully." : "Could not add the new employee. ID already exists.";
    }

    public String getEmployeeDetails(String id) {
        Employee employee = employeeService.getEmployeeDetails(id);
        if (employee == null) {
            return "Could not find employee.";
        }
        return "Found employee ID: " + employee.getId();
    }

    public String deactivateEmployee(String id){
        boolean success = employeeService.deactivateEmployee(id);
        return success ? "Employee deactivate successfully" : "Error: Could not find employee to deactivate.";

    }
}