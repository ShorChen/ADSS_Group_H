package presentation;

import domain.services.RoleService;
import presentation.controllers.RoleController;

public class ManageRolesUI extends View {
    RoleService service;
    private boolean open = false;
    private final RoleController roleController;
    private static final StringBuilder rolesMenu = new StringBuilder(
            """
                    0. Back
                    1. Create Role
                    """
    );

    Runnable onBack;

    public ManageRolesUI(Runnable onBack, RoleController roleController) {
        this.onBack = () -> {
            close();
            onBack.run();
        };
        this.roleController = roleController;
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
