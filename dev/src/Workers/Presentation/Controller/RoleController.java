package Workers.Presentation.Controller;

import Workers.Service.RoleService;

import java.util.ArrayList;
import java.util.List;

public class RoleController {
    private final RoleService service;

    public RoleController() {
        this.service = new RoleService();
    }

    public void createRole(String tag) {
        if (tag.trim().isEmpty())
            throw new IllegalArgumentException("Tag can not be empty");
        service.createRole(tag.trim());
    }

    public List<String> getAllRoles() {
        List<String> roles = new ArrayList<>();
        service.getAllRoles().forEach(role -> roles.add(role.getTag()));
        return roles;
    }

    public String getRole(String tag) {
        return service.getRole(tag).getTag();
    }

    public boolean containsRole(String tag){
        return service.containsRole(tag);
    }
}
