package data_access.pools;

import data_access.entities.EmployeeEntity;
import domain.entities.Role;
import domain.util.PasswordGenerator;

import java.util.HashMap;
import java.util.Map;

public class EmployeePool {
    private final Map<String, EmployeeEntity> employees;

    private static EmployeePool instance;
    public static EmployeePool Instance() {
        if (instance == null)
            instance = new EmployeePool();
        return instance;
    }

    private EmployeePool() {
        this.employees = new HashMap<>();

        String pass = PasswordGenerator.generatePassword();
        String pass2 = PasswordGenerator.generatePassword();

        EmployeeEntity e = new EmployeeEntity("1234", pass);
        EmployeeEntity e2 = new EmployeeEntity("4321", pass2);
        e.setQualifiedRoles(Role.MANAGER.getTag());


        addEmployee(e);
        addEmployee(e2);

        System.out.println("1234" + ", password: " + pass);
        System.out.println("4321" + ", password: " + pass2);
    }

    public void addEmployee(EmployeeEntity employee) {
        employees.put(employee.getId(), employee);
    }

    public boolean containsEmployee(String id) {
        return getEmployee(id) != null;
    }

    public EmployeeEntity getEmployee(String id) {
        return employees.get(id);
    }

    public boolean exists(String id) {
        return employees.containsKey(id);
    }

    public boolean removeEmployee(String id) {
        if (!exists(id)) throw new IllegalArgumentException("No employee found");
        return employees.remove(id) != null;
    }


    public boolean updatePassword(String id, String oldPass, String password) {
        if (!exists(id)) throw new IllegalArgumentException("No employee found");
        return employees.get(id).setPassword(oldPass, password);
    }

}
