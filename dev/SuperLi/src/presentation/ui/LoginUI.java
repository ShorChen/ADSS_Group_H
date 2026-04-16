package presentation.ui;

import domain.services.AuthService;
import presentation.control.AuthController;

public class LoginUI extends View {

    private boolean open = false;
    AuthController controller = new AuthController(new AuthService());
    private final CloseLoginUI onLoginAction;

    public LoginUI(CloseLoginUI onLoginAction) {
        this.onLoginAction = onLoginAction;
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
            boolean logged = controller.login(id, pass);
            System.out.println(logged ? "Successfully Logged in" : "No user found in the system");
            if (logged) onLoginAction.onLogin(id, pass, controller.isManager(id));
        }

    }

    @Override
    public void close() {
        open = false;
    }
}
