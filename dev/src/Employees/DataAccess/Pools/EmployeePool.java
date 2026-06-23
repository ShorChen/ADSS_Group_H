package Employees.DataAccess.Pools;

import Employees.DataAccess.EmployeeDAO;
import Employees.DataAccess.Entities.EmployeeEntity;
import Employees.DataAccess.Entities.Keys.EmployeeKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePool implements EmployeeDAO {
    private final Map<EmployeeKey, EmployeeEntity> employees;

    private static EmployeePool instance;

    public static EmployeePool Instance() {
        if (instance == null)
            instance = new EmployeePool();
        return instance;
    }

    private EmployeePool() {
        this.employees = new HashMap<>();
    }

    public void addUpdateEmployee(EmployeeEntity employee) {
        employees.put(createKey(employee.id()), employee);
    }

    public EmployeeEntity getEmployee(String id) {
        return employees.get(createKey(id));
    }

    public boolean exists(String id) {
        return employees.containsKey(createKey(id));
    }

    public boolean updatePassword(String id, String oldPass, String newPass) {
        if (!exists(id)) throw new IllegalArgumentException("No employee found");
        EmployeeEntity employee = employees.get(createKey(id));
        if (!employee.checkPassword(oldPass)) return false;
        addUpdateEmployee(employee.changePassword(newPass));
        return true;
    }

    public void deactivateEmployee(String id) {
        if (!exists(id)) throw new IllegalArgumentException("No employee found");
        EmployeeKey key = createKey(id);
        EmployeeEntity employee = employees.get(key);
        addUpdateEmployee(employee.changeActivityStatus(false));
    }

    public List<EmployeeEntity> getEmployeesWithRole(String roles) {
        List<EmployeeEntity> employeeEntityList = new ArrayList<>();
        employees.forEach((_, e) -> {
            if (e.qualifiedRoles().contains(roles))
                employeeEntityList.add(e);
        });
        return employeeEntityList;
    }

    public static void free() {
        instance = null;
    }

    private EmployeeKey createKey(String id) {
        return new EmployeeKey(id);
    }
}
