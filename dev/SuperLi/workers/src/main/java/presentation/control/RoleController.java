package presentation.control;

import domain.entities.Role;
import domain.services.RoleService;

import java.util.List;

/* requirement no. 5 */
public class RoleController {
    private final RoleService service;

    public RoleController() {
        this.service = new RoleService();
    }

    public boolean createRole(String tag) {
        if (tag.trim().isEmpty())
            throw new IllegalArgumentException("Tag can not be empty");
        return service.createRole(tag);
    }

    public List<Role> getAllRoles() {
        return service.getAllRoles();
    }

    public Role getRole(String tag) {
        return service.getRole(tag);
    }
}
