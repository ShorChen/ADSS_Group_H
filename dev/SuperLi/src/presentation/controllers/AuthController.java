package presentation.controllers;

import domain.services.AuthService;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public boolean login(String id, String pass) {
        return authService.login(id, pass);
    }
    
    public void logout() {
        authService.logout();
    }

    public boolean isManager(String id) {
        try {
            return authService.isManager(id);
        } catch (Exception e) {
            return false;
        }
    }
}