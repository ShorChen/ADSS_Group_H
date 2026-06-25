package Core.DataAccess;

import Core.Domain.Role;
import java.util.Map;

public interface AuthDAO {
    void addAuthAccount(String employeeId, String password, Role role);
    void removeAuthAccount(String employeeId);
    void updateAuthRole(String employeeId, Role role);
    void updatePassword(String employeeId, String newPassword);
    Role getRole(String employeeId);
    Map<String, Role> getAllAuthAccounts();
    Role login(String id, String password);
    String getFirstUserIdByRole(Role role);
    boolean verifyPassword(String id, String password);
}