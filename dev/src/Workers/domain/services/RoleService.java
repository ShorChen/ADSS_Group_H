package Workers.domain.services;

import Workers.data_access.pools.RolePool;
import Workers.domain.entities.Role;

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
        if (pool.containsRole(tag))
            return new Role(tag);
        return null;
    }

    public boolean containsRole(String tag) {
        return pool.containsRole(tag);
    }

    public void createRole(String s) {
        pool.addRole(s);
    }
}
