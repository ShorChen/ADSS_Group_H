package presentation.ui;

import context.SessionManager;
import presentation.control.AuthController;

public class ManagerHomeUI extends View {
    public static final StringBuilder managerMenu = new StringBuilder(
            """
                    Actions for manager:
                    0. Logout
                    1. View Shift History
                    2. Manage shifts
                    3. Manage Employees
                    4. Manage Roles
                    5. Change Password
                    """
    );
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
            System.out.print(managerMenu);
            int selection = getNextInteger("Select option (number)");
            handleSelection(selection,
                    onLogout,
                    this::viewShiftHistory,
                    this::manageShifts,
                    this::manageEmployees,
                    this::manageRoles,
                    this::changePassword
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