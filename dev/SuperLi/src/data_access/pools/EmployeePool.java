package data_access.pools;

import domain.entities.Employee;
import java.util.HashMap;
import java.util.Map;

public class EmployeePool {
    private final Map<String, Employee> employees;

    public EmployeePool() {
        this.employees = new HashMap<>();
    }

    public void addEmployee(Employee employee) {
        employees.put(employee.getId(), employee);
    }

    public Employee getEmployee(String id) {
        return employees.get(id);
    }

    public boolean exists(String id) {
        return employees.containsKey(id);
    }

    public boolean removeEmpoyee(String id){
        if (employees.containsKey(id)){
            employees.remove(id);
            return true;
        }
        return false;
    }
}
