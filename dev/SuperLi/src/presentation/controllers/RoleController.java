package presentation.controllers;

import domain.services.RoleService;

public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    public String addRole(String roleName) {
        return "Role logic will be implemented here.";
    }
}