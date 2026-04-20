package presentation.ui_manager;

import presentation.control.RoleController;
import presentation.ui_shared.View;
import presentation.util.Option;

public class ManageRolesUI extends View {
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
                    .append("Create Role", this::createRole), "Roles: " + controller.getAllRoles()
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
