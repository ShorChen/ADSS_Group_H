package Workers.Presentation.UIManager;

import Workers.Presentation.Controller.AuthController;
import Workers.Presentation.UIShared.ViewCLI;
import Workers.Presentation.Utils.Option;

public class ManagerHomeUI extends ViewCLI {
    private boolean open = false;
    private final AuthController controller;

    public ManagerHomeUI(Runnable onDismiss) {
        super(onDismiss);
        controller = new AuthController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            displayMenu(new Option.Builder("Actions for manager:")
                    .append("Logout", onDismiss)
                    .append("Change Password", this::changePassword)
                    .append("Manage Branches", this::managerBranches)
                    .append("Manage Roles", this::manageRoles)
            );

        }
    }


    private void changePassword() {
        String oldPassword = getNextLine("Enter Old Password");
        String password = getNextLine("Enter New Password");
        try {
            boolean passChanged = controller.changeCurrentEmployeePassword(oldPassword, password);
            if (passChanged)
                System.out.println("Password Changed");
            else System.out.println("The two passwords do not match");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void managerBranches() {
        ManageBranchesUI manageBranchesUI = new ManageBranchesUI(this::display);
        close();
        manageBranchesUI.display();
    }

    private void manageRoles() {
        ManageRolesUI manageRolesUI = new ManageRolesUI(this::display);
        close();
        manageRolesUI.display();
    }

    @Override
    public void close() {
        open = false;
    }
}