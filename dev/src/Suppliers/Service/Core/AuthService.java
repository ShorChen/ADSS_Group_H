package Suppliers.Service.Core;

import Suppliers.Domain.Facades.AuthFacade;
import Suppliers.Domain.Security.Role;
import Suppliers.Service.Response;

@SuppressWarnings("ClassCanBeRecord")
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