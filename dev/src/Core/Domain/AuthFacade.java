package Core.Domain;

import Core.DataAccess.AuthDAO;
import Suppliers.Domain.Security.SessionManager;

import java.util.Map;
import java.util.NoSuchElementException;

@SuppressWarnings("ClassCanBeRecord")
public class AuthFacade {
    private final Map<String, Role> validCodes;
    // private final AuthDAO authDAO;
    // if we will implement a window for an admin

    public AuthFacade(AuthDAO authDAO) {
        // this.authDAO = authDAO;
        validCodes = authDAO.getAllCodes();
    }

    public Role login(String code) {
        Role role = validCodes.get(code);
        if (role == null) throw new NoSuchElementException("Invalid login code.");
        SessionManager.getInstance().login(role);
        return role;
    }

    public void logout() {
        SessionManager.getInstance().logout();
    }

    public Role getCurrentRole() {
        return SessionManager.getInstance().getCurrentRole();
    }
}