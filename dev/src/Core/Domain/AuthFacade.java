package Core.Domain;

import Core.DataAccess.AuthDAO;

import java.util.NoSuchElementException;

@SuppressWarnings("ClassCanBeRecord")
public class AuthFacade {
    private final AuthDAO authDAO;

    public AuthFacade(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public Role login(String id, String password) {
        Role role = authDAO.login(id, password);
        if (role == null) throw new NoSuchElementException("Invalid Employee ID or Password.");
        SessionManager.getInstance().login(role);
        return role;
    }

    public Role autoLoginSystemTask(Role role) {
        String employeeId = authDAO.getFirstUserIdByRole(role);
        if (employeeId == null)
            throw new IllegalArgumentException("CRITICAL: No employee found in database with role " + role);
        SessionManager.getInstance().login(role);
        return role;
    }

    public boolean verifyPassword(String id, String password) {
        return authDAO.verifyPassword(id, password);
    }

    public void logout() {
        SessionManager.getInstance().logout();
    }

    public Role getCurrentRole() {
        return SessionManager.getInstance().getCurrentRole();
    }
}