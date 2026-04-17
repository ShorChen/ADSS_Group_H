package presentation.ui;

import presentation.control.RoleController;

public class ManageRolesUI extends View {
    RoleController controller;
    private boolean open = false;
    private static final StringBuilder rolesMenu = new StringBuilder(
            """
                    0. Back
                    1. Create Role
                    """
    );

    Runnable onBack;

    public ManageRolesUI(Runnable onBack) {
        this.onBack = onBack;
        controller = new RoleController();
    }

    @Override
    void display() {
        open = true;
        while (open) {
            System.out.println("Roles: " + controller.getAllRoles());
            System.out.print(rolesMenu);
            int selection = getNextInteger("Select option (number)");
            handleSelection(selection,
                    onBack,
                    this::createRole
            );
        }
    }

    private void createRole() {
        String newRole = getNextLine("Enter Role Name");
        boolean added = controller.createRole(newRole);
        System.out.println(added ? "new role added" : "Role is already present in the system");
    }


    @Override
    void close() {
        open = false;
    }
}
