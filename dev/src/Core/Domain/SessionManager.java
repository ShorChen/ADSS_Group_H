package Core.Domain;

public class SessionManager {
    private static SessionManager instance;
    private Role currentRole;

    private SessionManager() {
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null)
            instance = new SessionManager();
        return instance;
    }

    public void login(Role role) {
        if (currentRole != null) throw new IllegalStateException("Someone is already logged in.");
        currentRole = role;
    }

    public void logout() {
        if (currentRole == null) throw new IllegalStateException("No one is currently logged in.");
        currentRole = null;
    }

    public Role getCurrentRole() {
        if (currentRole == null) throw new SecurityException("Access Denied: You must log in first.");
        return currentRole;
    }

    public void requireRole(Role requiredRole) {
        if (getCurrentRole() != requiredRole)
            throw new SecurityException("Access Denied: Requires " + requiredRole + " role.");
    }
}