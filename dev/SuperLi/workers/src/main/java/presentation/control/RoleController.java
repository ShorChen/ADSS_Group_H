package presentation.control;

import domain.entities.Role;
import domain.services.RoleService;

import java.util.ArrayList;
import java.util.List;

public class RoleController {
    private final RoleService service;

    public RoleController() {
        this.service = new RoleService();
    }

    public boolean createRole(String tag) {
        if (tag.trim().isEmpty())
            throw new IllegalArgumentException("Tag can not be empty");
        boolean contains = service.containsRole(tag);
        service.createRole(tag.trim());
        return contains;
    }

    public List<String> getAllRoles() {
        List<String> roles = new ArrayList<>();
        service.getAllRoles().forEach(role -> roles.add(role.getTag()));
        return roles;
    }

    public String getRole(String tag) {
        return service.getRole(tag).getTag();
    }
}
