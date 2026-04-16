package domain.services;

import context.SessionManager;
import data_access.pools.AuthPool;
import domain.entities.Employee;
import domain.entities.Role;
import domain.util.PasswordGenerator;

public class AuthService {
    private final AuthPool authPool;

    public AuthService(AuthPool authPool) {
        this.authPool = authPool;
    }

    public String addWorker(Employee employee) {
        if (authPool.getUser(employee.getId()) != null) {
            throw new IllegalArgumentException("An employee with the given id is already present in the system");
        }
        String pass = generateFirstPassword();
        authPool.saveUser(employee, pass);
        return pass;
    }

    public boolean login(String id, String password) {
        String savedPassword = authPool.getPassword(id);
        if (savedPassword.equals(password)) {
            Employee emp = authPool.getUser(id);
            SessionManager.login(emp);
            return true;
        }
        return false;
    }

    public void logout() {
        SessionManager.logout();
    }

    public boolean changePassword(String id, String oldPass, String newPass) {
        if (!authPool.getPassword(id).equals(oldPass)) {
            return false;
        }
        if (PasswordGenerator.validatePassword(newPass)) {
            authPool.updatePassword(id, newPass);
            return true;
        }
        throw new IllegalArgumentException("password is invalid...");
    }

    public boolean isManager(String id) {
        Employee emp = authPool.getUser(id);
        if (emp == null) {
            throw new IllegalArgumentException("User not found");
        }
        return emp.getQualifiedRoles() != null && emp.getQualifiedRoles().contains(Role.MANAGER);
    }

    private String generateFirstPassword() {
        String pass = PasswordGenerator.generatePassword();
        while (authPool.isPasswordTaken(pass)) {
            pass = PasswordGenerator.generatePassword();
        }
        return pass;
    }
}