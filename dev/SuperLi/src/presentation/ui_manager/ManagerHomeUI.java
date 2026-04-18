package presentation.ui_manager;

import context.SessionManager;
import presentation.control.AuthController;
import presentation.ui_shared.View;
import presentation.util.Option;

public class ManagerHomeUI extends View {
    private boolean open = false;
    private final Runnable onLogout;
    private final AuthController controller;

    public ManagerHomeUI(Runnable onLogout) {
        this.onLogout = onLogout;
        controller = new AuthController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            displayMenu("Actions for manager:", "",
                    new Option("Logout", onLogout),
                    new Option("Change Password", this::changePassword),
                    new Option("View Shift History", this::viewShiftHistory),
                    new Option("Manage shifts", this::manageShifts),
                    new Option("Manage Employees", this::manageEmployees),
                    new Option("Manage Roles", this::manageRoles)
            );

        }
    }

    private void changePassword() {
        String oldPassword = getNextLine("Enter Old Password");
        String password = getNextLine("Enter New Password");
        try {
            boolean passChanged = controller.changePassword(SessionManager
                    .getCurrentEmployee().getId(), oldPassword, password);
            if (passChanged)
                System.out.println("Password Changed");
            else System.out.println("The two passwords do not match");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void manageRoles() {
        ManageRolesUI manageRolesUI = new ManageRolesUI(this::display);
        close();
        manageRolesUI.display();
    }

    private void manageEmployees() {
        ManageEmployeesUI manageEmployeesUI = new ManageEmployeesUI(this::display);
        close();
        manageEmployeesUI.display();
    }

    private void manageShifts() {
        ManageShiftsUI manageShiftsUI = new ManageShiftsUI(this::display);
        close();
        manageShiftsUI.display();
    }

    private void viewShiftHistory() {
        ViewShiftHistoryUI historyUI = new ViewShiftHistoryUI(this::display);
        close();
        historyUI.display();
    }

    @Override
    public void close() {
        open = false;
    }
}