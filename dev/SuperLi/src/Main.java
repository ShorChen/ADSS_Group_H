import presentation.ui.EmployeeHomeUI;
import presentation.ui.LoginUI;
import presentation.ui.ManagerHomeUI;

public class Main {
    private static LoginUI loginUI;
    private static ManagerHomeUI managerHomeUI;
    private static EmployeeHomeUI employeeHomeUI;

    public static void main(String[] args) {
        loginUI = new LoginUI(Main::onLoginAction);
        managerHomeUI = new ManagerHomeUI(Main::onLogoutAction);
        employeeHomeUI = new EmployeeHomeUI(Main::onLogoutAction);

        loginUI.display();
    }


    private static void onLogoutAction() {
        managerHomeUI.close();
        employeeHomeUI.close();
        loginUI.display();
    }

    private static void onLoginAction(String id, String pass, boolean manager) {
        loginUI.close();
        if (manager) {
            managerHomeUI.display();
        } else {
            employeeHomeUI.display();
        }
    }
}