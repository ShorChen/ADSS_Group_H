package Core.Domain;

import Core.DataAccess.AuthDAO;
import Suppliers.Domain.Security.SessionManager;

import java.util.Map;
import java.util.NoSuchElementException;

@SuppressWarnings("ClassCanBeRecord")
public class AuthFacade {
    private final Map<String, Managers> validCodes;
    // private final AuthDAO authDAO;
    // if we will implement a window for an admin

    public AuthFacade(AuthDAO authDAO) {
        // this.authDAO = authDAO;
        validCodes = authDAO.getAllCodes();
    }

    public Managers login(String code) {
        Managers managers = validCodes.get(code);
        if (managers == null) throw new NoSuchElementException("Invalid login code.");
        SessionManager.getInstance().login(managers);
        return managers;
    }

    public void logout() {
        SessionManager.getInstance().logout();
    }

    public Managers getCurrentRole() {
        return SessionManager.getInstance().getCurrentRole();
    }
}