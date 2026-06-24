package Employees.Presentation.UIEmployee;

import Employees.Context.SessionManager;
import Employees.Presentation.Controller.AuthController;
import Employees.Presentation.UIShared.ViewCLI;
import Employees.Presentation.Utils.Option;

public class EmployeeHomeUI extends ViewCLI {
    private boolean open = false;
    private final AuthController authController;

    public EmployeeHomeUI(Runnable onDismiss) {
        super(onDismiss);
        authController = new AuthController();
    }


    @Override
    public void display() {
        open = true;
        while (open) {
            displayMenu(new Option.Builder("Actions for Employee")
                    .append("Logout", () -> {
                        SessionManager.unselectBranch();
                        onDismiss.run();
                    })
                    .append("Change Password", this::changePassword)
                    .append("View Schedule & Submit Availability", this::chooseShifts)
                    .append("Request Shift Replacement", this::requestReplacement)
                    .append("Report Additional Hours", this::reportAdditionalHours));
        }
    }

    private void reportAdditionalHours() {
        EmployeeAdditionalHoursUI additionalHours = new EmployeeAdditionalHoursUI(this::display);
        close();
        additionalHours.display();
    }

    private void changePassword() {
        String oldPassword = getNextLine("Enter Old Password");
        String password = getNextLine("Enter New Password");
        try {
            boolean passChanged = authController.changeCurrentEmployeePassword(oldPassword, password);
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
        RequestReplacementUI requestUI = new RequestReplacementUI(this::display);
        close();
        requestUI.display();
    }

    @Override
    public void close() {
        open = false;
    }
}