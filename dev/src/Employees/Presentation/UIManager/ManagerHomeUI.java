package Employees.Presentation.UIManager;

import Employees.Presentation.Controller.AuthController;
import Employees.Presentation.Controller.ManagerHomeController;
import Employees.Presentation.UIEmployee.RequestReplacementUI;
import Employees.Presentation.UIShared.ViewCLI;
import Employees.Presentation.Utils.Option;

public class ManagerHomeUI extends ViewCLI {
    private boolean open = false;
    private final ManagerHomeController controller;
    private final AuthController authController;

    public ManagerHomeUI(Runnable onDismiss) {
        super(onDismiss);
        controller = new ManagerHomeController();
        authController = new AuthController();
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
                    .append("Set Submission Deadline", this::setDeadline)
                    .append("Handle Replacement Requests", this::handleReplacements)
                    .append("Issue HR Report (Not Implemented)", this::issueReport)

            );

        }
    }


    private void changePassword() {
        String oldPassword = getNextLine("Enter Old Password");
        String password = getNextLine("Enter New Password");
        try {
            boolean passChanged = authController
                    .changeCurrentEmployeePassword(oldPassword, password);
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


    private void setDeadline() {
        boolean validDate = false;
        if (controller.isFirstWeek()) return;
        while (!validDate) {
            String date = getNextLine("Enter Date For Deadline (dd/MM/yyyy HH:mm) or type cancel to cancel: ");
            if (date.equalsIgnoreCase("cancel")) {
                System.out.println("Canceled");
                return;
            }
            try {
                controller.setDeadline(date);
                validDate = true;
                System.out.println("Deadline set to " + date);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void handleReplacements() {
        RequestReplacementUI requestUI = new RequestReplacementUI(this::display);
        close();
        requestUI.display();
    }

    private void issueReport() {
        System.out.println(controller.issueReport());
    }


    @Override
    public void close() {
        open = false;
    }
}