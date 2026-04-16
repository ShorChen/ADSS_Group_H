package domain.services;

import data_access.pools.RolePool;
import domain.entities.Role;

public class RoleService {
    private final RolePool rolePool;

    public RoleService(RolePool rolePool) {
        this.rolePool = rolePool;
    }

    public boolean createRole(String name){
        if (rolePool.getRole(name) != null) {
            return false; 
        }
        
        rolePool.addRole(new Role(name));
        return true;
    }
}