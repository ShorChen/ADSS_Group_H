package domain.services;

import data_access.entities.EmployeeEntity;
import data_access.pools.EmployeePool;
import domain.entities.Employee;
import domain.entities.Role;
import domain.util.PasswordGenerator;

public class AuthService {
    private final EmployeePool employees;

    public AuthService() {
        employees = EmployeePool.Instance();

    }

    public boolean changePassword(String id, String oldPass, String newPass) {
        if (validatePassword(newPass))
            return employees.updatePassword(id, oldPass, newPass);
        throw new IllegalArgumentException(
                "password is invalid, a password should contain at least 8 characters," +
                " a digit, a lowercase and uppercase characters and a special" +
                " character !@#$%^&*()-_=+[]{}|;:,.<>?\"");
    }

    public Employee login(String id, String password) {
        EmployeeEntity entity = employees.getEmployee(id);
        if (entity != null && entity.checkPassword(password))
            return new Employee(entity);
        return null;
    }

    private boolean validatePassword(String password) {
        return PasswordGenerator.validatePassword(password);
    }

    public boolean isManager(String id) {
        if (employees.exists(id))
            return employees.getEmployee(id).getQualifiedRoles().contains(Role.MANAGER.getTag());
        throw new IllegalArgumentException("No employee found");
    }

}
