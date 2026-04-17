package presentation.ui;

import context.SessionManager;
import domain.util.PasswordGenerator;
import presentation.control.AuthController;
import presentation.control.EmployeeController;

import java.util.InputMismatchException;

public class EmployeeHomeUI extends View {
    public static final StringBuilder employeeMenu = new StringBuilder(
            """
                    Actions for Employee:
                    0. Logout
                    1. View Schedule & Submit Availability
                    2. Set Preferred Rest Day
                    3. Change Password
                    4. Request Shift Replacement
                    """
    );
    private boolean open = false;
    private final Runnable onLogout;
    private EmployeeController controller;
    private AuthController authController;

    public EmployeeHomeUI(Runnable onLogout) {
        this.onLogout = onLogout;
        controller = new EmployeeController();
        authController = new AuthController();
    }


    @Override
    public void display() {
        open = true;
        while (open) {
            System.out.print(employeeMenu.toString());
            int selection = getNextInteger("Select option (number)");
            handleSelection(selection,
                    onLogout,
                    this::chooseShifts,
                    this::setRestDay,
                    this::changePassword,
                    this::requestReplacement
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

    private void setRestDay() {
        int day = getNextInteger("Enter day (0=SUN, 1=MON, 2=TUE, 3=WED, 4=THU, 5=FRI, 6=SAT):");


    }

    private void requestReplacement() {
        System.out.println("not implemented");
    }

    @Override
    public void close() {
        open = false;
    }
}