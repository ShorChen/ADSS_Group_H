package domain.in_memory;

import domain.entities.Role;

import java.util.List;

public class RolePool {
    private final List<Role> roles;

    public RolePool(List<Role> roles) {
        this.roles = roles;
    }

    public Role getEmployee(String tag) {
        for (Role r : roles)
            if (r.getTag().equals(tag))
                return r;
        return null;
    }
}
