package presentation.ui_shared;

import presentation.ui_employee.EmployeeHomeUI;
import presentation.ui_manager.ManagerHomeUI;

public class FirstScreenUI extends View {
    private LoginUI loginUI;
    private ManagerHomeUI managerHomeUI;
    private EmployeeHomeUI employeeHomeUI;

    @Override
    public void display() {
        loginUI = new LoginUI(this::onLoginAction);
        managerHomeUI = new ManagerHomeUI(this::onLogoutAction);
        employeeHomeUI = new EmployeeHomeUI(this::onLogoutAction);

        loginUI.display();


    }

    @Override
    public void close() {

    }

    private void onLogoutAction() {
        managerHomeUI.close();
        employeeHomeUI.close();
        loginUI.display();
    }

    private void onLoginAction(String id, String pass, boolean manager) {
        loginUI.close();
        if (manager) {
            managerHomeUI.display();
        } else {
            employeeHomeUI.display();
        }
    }
}