package Core.Domain;

public class SessionManager {
    private static SessionManager instance;
    private Role currentRole;
    private String currentUserId;

    private SessionManager() {
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null)
            instance = new SessionManager();
        return instance;
    }

    public void login(String userId, Role role) {
        if (currentRole != null) throw new IllegalStateException("Someone is already logged in.");
        currentUserId = userId;
        currentRole = role;
    }

    public void logout() {
        if (currentRole == null) throw new IllegalStateException("No one is currently logged in.");
        currentUserId = null;
        currentRole = null;
    }

    public Role getCurrentRole() {
        if (currentRole == null) throw new SecurityException("Access Denied: You must log in first.");
        return currentRole;
    }

    public String getLoggedInUserId() {
        if (currentUserId == null) throw new SecurityException("Access Denied: You must log in first.");
        return currentUserId;
    }

    public void requireRole(Role requiredRole) {
        if (getCurrentRole() != requiredRole)
            throw new SecurityException("Access Denied: Requires " + requiredRole + " role.");
    }
}