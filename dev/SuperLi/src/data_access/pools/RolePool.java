package data_access.pools;

import domain.entities.Role;

import java.util.ArrayList;
import java.util.List;

public class RolePool {
    private final List<Role> roles;

    public RolePool() {
        this.roles = new ArrayList<>();
        
        roles.add(Role.MANAGER);
        roles.add(Role.Storekeeper);
        roles.add(Role.ShiftManager);
        roles.add(Role.Cashier);
    }

    public Role getRole(String tag) {
        for (Role r : roles)
            if (r.getTag().equals(tag))
                return r;
        return null;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public Role getEmployee(String tag) {
        for (Role r : roles)
            if (r.getTag().equals(tag))
                return r;
        return null;
    }
}
