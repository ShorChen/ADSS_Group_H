package presentation;

import contex.SessionManager;
import domain.AuthFacade;

public class AuthService {
    private final AuthFacade facade;
    public AuthService(AuthFacade facade){
        this.facade = facade;
    }
    public boolean login(String id, String password){
        SessionManager.login(facade.login(id, password));
        return SessionManager.hasContext();
    }
    public void logout(){
        SessionManager.logout();
    }
}
