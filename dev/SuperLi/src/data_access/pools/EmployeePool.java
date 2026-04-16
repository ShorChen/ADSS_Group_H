package data_access.pools;

import domain.entities.Employee;

import java.util.List;

public class EmployeePool {
    private final List<Employee> employees;

    public EmployeePool(List<Employee> employees) {
        this.employees = employees;
    }

    public Employee getEmployee(String id) {
        for (Employee e : employees)
            if (e.getId().equals(id))
                return e;
        return null;
    }
}
