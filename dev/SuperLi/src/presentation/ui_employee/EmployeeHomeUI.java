package presentation.ui_employee;

import context.SessionManager;
import presentation.control.AuthController;
import presentation.ui_shared.View;
import presentation.util.Option;

public class EmployeeHomeUI extends View {
    private boolean open = false;
    private final Runnable onLogout;
    private final AuthController authController;

    public EmployeeHomeUI(Runnable onLogout) {
        this.onLogout = onLogout;
        authController = new AuthController();
    }


    @Override
    public void display() {
        open = true;
        while (open) {
            displayMenu("Actions for Employee", "",
                    new Option("Logout", onLogout),
                    new Option("Change Password", this::changePassword),
                    new Option("View Schedule & Submit Availability", this::chooseShifts),
                    new Option("Request Shift Replacement", this::requestReplacement)
            );
        }
    }

    private void changePassword() {
        String oldPassword = getNextLine("Enter Old Password");
        String password = getNextLine("Enter New Password");
        try {
            boolean passChanged = authController.changePassword(SessionManager
                    .getCurrentEmployee().getId(), oldPassword, password);
            if (passChanged)
                System.out.println("Password Changed");
            else System.out.println("The two passwords do not match");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private void chooseShifts() {
        EmployeeChooseShiftsUI shiftsUI = new EmployeeChooseShiftsUI(this::display);
        close();
        shiftsUI.display();
    }


    private void requestReplacement() {
        System.out.println("not implemented");
    }

    @Override
    public void close() {
        open = false;
    }
}