package data_access.pools;

import data_access.entities.EmployeeEntity;
import domain.entities.Role;
import domain.util.PasswordGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        EmployeeEntity e3 = new EmployeeEntity("<SHIFT MANAGER ID>", pass2);
        EmployeeEntity e4 = new EmployeeEntity("cashier", pass2);
        e.setQualifiedRoles(Role.MANAGER.getTag());
        e2.setQualifiedRoles(Role.Cashier.getTag());
        e3.setQualifiedRoles(Role.ShiftManager.getTag());
        e4.setQualifiedRoles(Role.ShiftManager.getTag(), Role.Cashier.getTag());


        addEmployee(e);
        addEmployee(e2);
        addEmployee(e3);

        System.out.println("1234" + ", password: " + pass);
        System.out.println("4321" + ", password: " + pass2);
    }

    public void addEmployee(EmployeeEntity employee) {
        employees.put(employee.getId(), employee);
    }


    public EmployeeEntity getEmployee(String id) {
        return employees.get(id);
    }

    public boolean exists(String id) {
        return employees.containsKey(id);
    }

    public boolean updatePassword(String id, String oldPass, String password) {
        if (!exists(id)) throw new IllegalArgumentException("No employee found");
        return employees.get(id).setPassword(oldPass, password);
    }

    public List<EmployeeEntity> getEmployees(String tag) {
        List<EmployeeEntity> employeeEntityList = new ArrayList<>();
        employees.forEach((_, e) -> {
            if (e.getQualifiedRoles().contains(tag))
                employeeEntityList.add(e);
        });
        return employeeEntityList;
    }

    public boolean containsRole(String id, String role) {
        if (!exists(id)) return false;
        return employees.get(id).getQualifiedRoles().contains(role);
    }

    public void clear() {
        if (this.employees != null) {
            this.employees.clear();
        }
    }


}
