package domain;

import java.util.HashMap;
import java.util.Map;

public class AuthFacade {
    private final Map<String, String> employees; // employee id to their password
    private final Map<String, Employee> idToEmployee;

    public AuthFacade() {
        employees = new HashMap<>();
        idToEmployee = new HashMap<>();
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
}
