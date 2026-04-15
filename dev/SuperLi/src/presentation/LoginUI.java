package presentation;

import domain.facades.AuthFacade;
import service.services.AuthService;

public class LoginUI extends View {

    private boolean open = false;
    AuthService service = new AuthService(new AuthFacade());
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
            boolean logged = service.login(id, pass);
            System.out.println(logged ? "Successfully Logged in" : "No user found in the system");
            if (logged) onLoginAction.onLogin(id, pass, service.isManager(id));
        }

    }

    @Override
    public void close() {
        open = false;
    }
}
