package Employees.DataAccess;

import java.util.List;

public interface RoleDAO {
    boolean containsRole(String role);

     void addRole(String role);

     List<String> getAllRoles();
}
