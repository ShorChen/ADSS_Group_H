package Suppliers.DataAccess;

import Suppliers.Domain.Security.Role;

import java.util.Map;

public interface AuthDAO {
    void addCode(String code, Role role);

    void removeCode(String code);

    void updateCode(String code, Role role);

    Role getRole(String code);

    Map<String, Role> getAllCodes();
}