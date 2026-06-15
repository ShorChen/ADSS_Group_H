package Workers.DataAccess.Pools;

import Workers.Domain.Entities.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RolePool {
    private final Set<String> roles;

    private static RolePool instance;

    public static RolePool Instance() {
        if (instance == null)
            instance = new RolePool();
        return instance;
    }

    private RolePool() {
        this.roles = new HashSet<>();

        roles.add(Role.MANAGER.getTag());
        roles.add(Role.Storekeeper.getTag());
        roles.add(Role.ShiftManager.getTag());
        roles.add(Role.Cashier.getTag());
    }

    public static void free(){
        instance = null;
    }

    public boolean containsRole(String role){
        return roles.contains(role);
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public List<String> getAllRoles() {
        return new ArrayList<>(roles);
    }
}
