package presentation.ui;

import presentation.control.AuthController;

public class LoginUI extends View {

    private boolean open = false;
    AuthController controller = new AuthController();
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
            if (logged) {
                System.out.println("Successfully Logged in");
                onLoginAction.onLogin(id, pass, controller.isManager(id));
            } else System.out.println("Id or password are Invalid");

        }

    }

    @Override
    public void close() {
        open = false;
    }
}
