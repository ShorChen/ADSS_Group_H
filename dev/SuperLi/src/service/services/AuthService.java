package service.services;

import context.SessionManager;
import domain.facades.AuthFacade;

public class AuthService {
    private final AuthFacade facade;

    public AuthService(AuthFacade facade) {
        this.facade = facade;
    }

    public boolean registerManager(String id, String password){
        //todo
        return false;
    }
    public boolean login(String id, String password) {
        SessionManager.login(facade.login(id, password));
        return SessionManager.hasContext();
    }

    public void logout() {
        SessionManager.logout();
    }

    public boolean changePassword(String id, String oldPass, String newPass) {
        return facade.changePassword(id, oldPass, newPass);
    }

    public boolean isManager(String id){
        return facade.isManager(id);
    }
}
