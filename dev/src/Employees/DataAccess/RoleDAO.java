package Employees.DataAccess;
import java.util.List;

public interface RoleDAO {
    void addRole(String role);
    List<String> getAllRoles();
}