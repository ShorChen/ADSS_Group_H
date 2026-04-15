package presentation;

import service.services.RoleService;

public class ManageRolesUI extends View {
    RoleService service;
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
        service = new RoleService();
    }

    @Override
    void display() {
        open = true;
        while (open) {
            System.out.println(rolesMenu.toString());
            int selection = getNextInteger("Select option (number)");
            handleSelection(selection,
                    onBack,
                    this::createRole
            );
        }
    }

    private void createRole() {
        String newRole = getNextLine("Enter Role Name");
        boolean added = service.createRole(newRole);
        System.out.println(added ? "new role added" : "Role is already present in the system");
    }


    @Override
    void close() {
        open = false;
    }
}
