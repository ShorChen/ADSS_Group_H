package Suppliers.Service;

import Suppliers.Domain.AuthFacade;
import Suppliers.Domain.Role;

public class AuthService {
    private final AuthFacade authFacade;

    public AuthService(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    public Response<Boolean> login(String code) {
        try {
            authFacade.login(code);
            return new Response<>(true);
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Boolean> logout() {
        try {
            authFacade.logout();
            return new Response<>(true);
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Role> getCurrentRole() {
        try {
            return new Response<>(authFacade.getCurrentRole());
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }
}