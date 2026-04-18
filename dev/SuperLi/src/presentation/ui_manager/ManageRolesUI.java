package presentation.ui_manager;

import presentation.control.RoleController;
import presentation.ui_shared.View;
import presentation.util.Option;

public class ManageRolesUI extends View {
    RoleController controller;
    private boolean open = false;

    Runnable onBack;

    public ManageRolesUI(Runnable onBack) {
        this.onBack = onBack;
        controller = new RoleController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            displayMenu("Managing Roles", "Roles: " + controller.getAllRoles(),
                    new Option("Back", onBack),
                    new Option("Create Role", this::createRole)
            );
        }
    }

    private void createRole() {
        String newRole = getNextLine("Enter Role Name");
        boolean added = controller.createRole(newRole);
        System.out.println(added ? "new role added" : "Role is already present in the system");
    }


    @Override
    public void close() {
        open = false;
    }
}
