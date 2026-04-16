package presentation.control;

import context.SessionManager;
import domain.services.AuthService;

public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    public boolean registerManager(String id, String password){
        //todo
        return false;
    }
    public boolean login(String id, String password) {
        SessionManager.login(service.login(id, password));
        return SessionManager.hasContext();
    }

    public void logout() {
        SessionManager.logout();
    }

    public boolean changePassword(String id, String oldPass, String newPass) {
        return service.changePassword(id, oldPass, newPass);
    }

    public boolean isManager(String id){
        return service.isManager(id);
    }
}
