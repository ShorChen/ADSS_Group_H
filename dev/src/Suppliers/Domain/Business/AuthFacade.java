package Suppliers.Domain.Business;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class AuthFacade {
    private final Map<String, Role> validCodes;

    public AuthFacade() {
        this.validCodes = new HashMap<>();
        this.validCodes.put("SUP123", Role.SUPPLIER_MANAGER);
        this.validCodes.put("ORD123", Role.ORDER_MANAGER);
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