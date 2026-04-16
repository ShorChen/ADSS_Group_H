package data_access.pools;

import domain.entities.Employee;
import java.util.HashMap;
import java.util.Map;

public class AuthPool {
    private final Map<String, String> passwords; // id -> password
    private final Map<String, Employee> users;   // id -> Employee

    public AuthPool() {
        this.passwords = new HashMap<>();
        this.users = new HashMap<>();
    }

    public void saveUser(Employee employee, String password) {
        users.put(employee.getId(), employee);
        passwords.put(employee.getId(), password);
    }

    public Employee getUser(String id) {
        return users.get(id);
    }

    public String getPassword(String id) {
        return passwords.getOrDefault(id, "");
    }

    public boolean isPasswordTaken(String password) {
        return passwords.containsValue(password);
    }
    
    public void updatePassword(String id, String newPassword) {
        passwords.put(id, newPassword);
    }
}
