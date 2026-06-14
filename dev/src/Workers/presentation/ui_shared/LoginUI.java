package Workers.presentation.ui_shared;

import Workers.context.SessionManager;
import Workers.presentation.control.AuthController;
import Workers.presentation.ui_employee.EmployeeHomeUI;
import Workers.presentation.ui_manager.ManagerHomeUI;

public class LoginUI extends ViewCLI {

    private boolean open = false;
    AuthController controller = new AuthController();

    public LoginUI(Runnable onDismiss) {
        super(onDismiss);
    }


    @Override
    public void display() {
        SessionManager.logout();
        System.out.println("---Welcome to SuperLi!---");
        open = true;
        while (open) {
            String id = getNextLine("Enter employee id:");
            String pass = getNextLine("Enter employee password:");

            boolean logged = controller.login(id, pass);
            if (logged) {
                System.out.println("Successfully Logged in");
                close();
                boolean manager = controller.isManager(id);
                if (manager) {
                    ManagerHomeUI managerHomeUI = new ManagerHomeUI(this::display);
                    managerHomeUI.display();
                } else {
                    EmployeeHomeUI employeeHomeUI = new EmployeeHomeUI(this::display);
                    employeeHomeUI.display();
                }
            } else System.out.println("Id or password are Invalid");

        }

    }

    @Override
    public void close() {
        open = false;
    }
}
