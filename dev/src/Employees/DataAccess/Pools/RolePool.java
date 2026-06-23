package Employees.DataAccess.Pools;

import Employees.DataAccess.RoleDAO;
import Employees.Domain.DTO.RoleSL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RolePool implements RoleDAO {
    private final Set<String> roles;

    private static RolePool instance;

    public static RolePool Instance() {
        if (instance == null)
            instance = new RolePool();
        return instance;
    }

    private RolePool() {
        this.roles = new HashSet<>();

        roles.add(RoleSL.MANAGER.getTag());
        roles.add(RoleSL.BRANCH_MANAGER.getTag());
        roles.add(RoleSL.Storekeeper.getTag());
        roles.add(RoleSL.ShiftManager.getTag());
        roles.add(RoleSL.Cashier.getTag());
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
