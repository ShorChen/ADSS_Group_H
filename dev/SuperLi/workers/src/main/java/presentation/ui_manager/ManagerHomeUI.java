package presentation.ui_manager;

import presentation.control.AuthController;
import presentation.ui_shared.View;
import presentation.util.Option;

public class ManagerHomeUI extends View {
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
                    .append("View Shift History", this::viewShiftHistory)
                    .append("Manage shifts", this::manageShifts)
                    .append("Manage Employees", this::manageEmployees)
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