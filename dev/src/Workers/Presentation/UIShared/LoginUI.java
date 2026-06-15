package Workers.Presentation.UIShared;

import Workers.Context.SessionManager;
import Workers.Presentation.Controller.AuthController;
import Workers.Presentation.UIEmployee.EmployeeHomeUI;
import Workers.Presentation.UIManager.ManagerHomeUI;

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
