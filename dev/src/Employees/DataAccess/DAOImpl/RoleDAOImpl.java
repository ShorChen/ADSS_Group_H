package Employees.DataAccess.DAOImpl;

import Employees.DataAccess.RoleDAO;

import java.util.List;

public class RoleDAOImpl implements RoleDAO {
    @Override
    public boolean containsRole(String role) {
        return false;
    }

    @Override
    public void addRole(String role) {

    }

    @Override
    public List<String> getAllRoles() {
        return List.of();
    }
}
