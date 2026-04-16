package domain.services;

import domain.entities.Employee;
import domain.entities.Role;
import domain.util.PasswordGenerator;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final Map<String, String> employees; // employee id to their password
    private final Map<String, Employee> idToEmployee;

    public AuthService() {
        employees = new HashMap<>();
        idToEmployee = new HashMap<>();

        Employee e = new Employee("1234");
        Employee e2 = new Employee("4321");
        e.setQualifiedRoles(Role.MANAGER);
        System.out.println("1234" + ", password: " + addWorker(e));
        System.out.println("4321" + ", password: " + addWorker(e2));
    }

    public String addWorker(Employee employee) {
        String pass = generateFirstPassword();
        if (!employees.containsKey(employee.getId())) {
            employees.put(employee.getId(), pass);
            idToEmployee.put(employee.getId(), employee);
            return pass;
        }
        throw new IllegalArgumentException("An employee with the given id is already present in the system");
    }

    public boolean changePassword(String id, String oldPass, String newPass) {
        if (!employees.getOrDefault(id, "").equals(oldPass))
            return false;
        if (validatePassword(newPass)) {
            employees.put(id, newPass);
            return true;
        }
        throw new IllegalArgumentException(
                "password is invalid, a password should contain at least 8 characters," +
                " a digit, a lowercase and uppercase characters and a special" +
                " character !@#$%^&*()-_=+[]{}|;:,.<>?\"");
    }

    public Employee login(String id, String password) {
        if (employees.getOrDefault(id, "").equals(password)) {
            return idToEmployee.get(id);
        }
        return null;
    }

    private boolean validatePassword(String password) {
        return PasswordGenerator.validatePassword(password);
    }

    private String generateFirstPassword() {
        String pass = PasswordGenerator.generatePassword();
        while (employees.containsValue(pass)) {
            pass = PasswordGenerator.generatePassword();
        }
        return pass;
    }

    public boolean isManager(String id){
        if (!idToEmployee.containsKey(id))
            throw new IllegalArgumentException("Only the HR Manager can perform this action");
        return idToEmployee.get(id).getQualifiedRoles().contains(Role.MANAGER);
    }
}
