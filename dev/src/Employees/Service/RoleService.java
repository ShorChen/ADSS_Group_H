package Employees.Service;

import Employees.DataAccess.RoleDAO;
import Employees.DataAccess.SqlImpl.SqlRoleDAO;
import Employees.Domain.DTO.RoleSL;

import java.util.ArrayList;
import java.util.List;

public class RoleService {
    private final RoleDAO roleDAO;

    public RoleService() {
        this.roleDAO = new SqlRoleDAO();
    }

    public List<RoleSL> getAllRoles() {
        List<RoleSL> roles = new ArrayList<>();
        for (String s : roleDAO.getAllRoles())
            roles.add(new RoleSL(s));
        return roles;
    }

    public RoleSL getRole(String tag) {
        if (roleDAO.containsRole(tag))
            return new RoleSL(tag);
        return null;
    }

    public boolean containsRole(String tag) {
        return roleDAO.containsRole(tag);
    }

    public void createRole(String s) {
        roleDAO.addRole(s);
    }
}