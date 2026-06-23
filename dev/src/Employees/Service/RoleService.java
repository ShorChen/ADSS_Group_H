package Employees.Service;

import Employees.DataAccess.Pools.RolePool;
import Employees.Domain.Entities.RoleSL;

import java.util.ArrayList;
import java.util.List;

public class RoleService {

    private final RolePool pool;

    public RoleService() {
        this.pool = RolePool.Instance();
    }

    public List<RoleSL> getAllRoles() {
        List<RoleSL> roles = new ArrayList<>();
        for (String s : pool.getAllRoles())
            roles.add(new RoleSL(s));
        return roles;
    }

    public RoleSL getRole(String tag) {
        if (pool.containsRole(tag))
            return new RoleSL(tag);
        return null;
    }

    public boolean containsRole(String tag) {
        return pool.containsRole(tag);
    }

    public void createRole(String s) {
        pool.addRole(s);
    }
}
