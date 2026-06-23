package Suppliers.Domain.Security;

import Core.Domain.Managers;

public class SessionManager {
    private static SessionManager instance;
    private Managers currentManagers;

    private SessionManager() {
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null)
            instance = new SessionManager();
        return instance;
    }

    public void login(Managers managers) {
        if (currentManagers != null) throw new IllegalStateException("Someone is already logged in.");
        currentManagers = managers;
    }

    public void logout() {
        if (currentManagers == null) throw new IllegalStateException("No one is currently logged in.");
        currentManagers = null;
    }

    public Managers getCurrentRole() {
        if (currentManagers == null) throw new SecurityException("Access Denied: You must log in first.");
        return currentManagers;
    }

    public void requireRole(Managers requiredManagers) {
        if (getCurrentRole() != requiredManagers)
            throw new SecurityException("Access Denied: Requires " + requiredManagers + " role.");
    }
}