package presentation.ui_manager;

import presentation.control.RoleController;
import presentation.ui_shared.ViewCLI;
import presentation.util.Option;

public class ManageRolesUI extends ViewCLI {
    private final RoleController controller;
    private boolean open = false;


    public ManageRolesUI(Runnable onDismiss) {
        super(onDismiss);
        controller = new RoleController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            displayMenu(new Option.Builder("Managing Roles")
                    .append("Back", onDismiss)
                    .append("Create Role", this::createRole)
                    .setEndMessage("Roles: " + controller.getAllRoles())
            );
        }
    }

    private void createRole() {
        String newRole = getNextLine("Enter Role Name");
        if (controller.containsRole(newRole))
            System.out.println("Role is already present in the system");
        else {
            controller.createRole(newRole);
            System.out.println("new role added");
        }
    }


    @Override
    public void close() {
        open = false;
    }
}
