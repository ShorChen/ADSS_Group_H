package Workers.Service;

import Workers.DataAccess.DAO.EmployeeDAO;
import Workers.DataAccess.Entities.EmployeeEntity;
import Workers.DataAccess.Pools.EmployeePool;
import Workers.Domain.DTO.EmployeeSL;
import Workers.Domain.DTO.RoleSL;
import Workers.Domain.Utils.PasswordGenerator;

public class AuthService {
    private final EmployeeDAO employees;

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

    public EmployeeSL login(String id, String password) {
        EmployeeEntity entity = employees.getEmployee(id);
        if (entity == null || !entity.active()) return null;
        if (entity.checkPassword(password))
            return new EmployeeSL(entity);
        return null;
    }

    private boolean validatePassword(String password) {
        return PasswordGenerator.validatePassword(password);
    }

    public boolean isManager(String id) {
        if (employees.exists(id))
            return employees.getEmployee(id).qualifiedRoles().contains(RoleSL.MANAGER.getTag());
        throw new IllegalArgumentException("No employee found");
    }

    public boolean isBranchManager(String id) {
        if (employees.exists(id))
            return employees.getEmployee(id).qualifiedRoles().contains(RoleSL.BRANCH_MANAGER.getTag());
        throw new IllegalArgumentException("No employee found");
    }

}
