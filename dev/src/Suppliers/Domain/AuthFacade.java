package Suppliers.Domain;

import Suppliers.DataAccess.AuthDAO;
import java.util.Map;
import java.util.NoSuchElementException;

public class AuthFacade {
    private final Map<String, Role> validCodes;
    private final AuthDAO authDAO;

    public AuthFacade(AuthDAO authDAO) {
        this.authDAO = authDAO;
        this.validCodes = authDAO.getAllCodes();
    }

    public void login(String code) {
        Role role = validCodes.get(code);
        if (role == null) throw new NoSuchElementException("Invalid login code.");
        SessionManager.getInstance().login(role);
    }

    public void logout() {
        SessionManager.getInstance().logout();
    }
}