package domain.services;

import data_access.pools.RolePool;
import domain.entities.Role;

import java.util.ArrayList;
import java.util.List;

public class RoleService {

    private final RolePool pool;

    public RoleService() {
        this.pool = RolePool.Instance();
    }

    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        for (String s : pool.getAllRoles())
            roles.add(new Role(s));
        return roles;
    }

    public Role getRole(String tag) {
        String role = pool.getRole(tag);
        if (role == null) return null;
        return new Role(role);
    }

    public boolean containsRole(String tag) {
        return pool.getRole(tag) != null;
    }

    public boolean createRole(String s) {
        return pool.addRole(s);
    }
}
