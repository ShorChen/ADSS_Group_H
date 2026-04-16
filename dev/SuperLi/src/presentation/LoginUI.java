package presentation;

import domain.services.AuthService;
import presentation.controllers.AuthController;

public class LoginUI extends View {

    private boolean open = false;
    private final AuthController authController;
    private final CloseLoginUI onLoginAction;

    public LoginUI(CloseLoginUI onLoginAction, AuthController authController) {
        this.onLoginAction = onLoginAction;
        this.authController = authController;
    }

    public interface CloseLoginUI {
        void onLogin(String id, String pass, boolean manager);
    }

    @Override
    public void display() {
        System.out.println("---Welcome to SuperLi!---");
        open = true;
        while (open) {
            String id = getNextLine("Enter employee id:");
            String pass = getNextLine("Enter employee password:");
            boolean logged = authController.login(id, pass);
            System.out.println(logged ? "Successfully Logged in" : "No user found in the system");
            if (logged) onLoginAction.onLogin(id, pass, authController.isManager(id));
        }

    }

    @Override
    public void close() {
        open = false;
    }
}
