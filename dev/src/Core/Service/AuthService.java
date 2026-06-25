package Core.Service;

import Core.Domain.AuthFacade;
import Core.Domain.Role;

@SuppressWarnings("ClassCanBeRecord")
public class AuthService {
    private final AuthFacade authFacade;

    public AuthService(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    public Response<Role> login(String id, String password) {
        try {
            Role role = authFacade.login(id, password);
            return new Response<>(role);
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

    @SuppressWarnings("unused")
    public Response<Role> getCurrentRole() {
        try {
            return new Response<>(authFacade.getCurrentRole());
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }

    public Response<Role> autoLoginSystemTask(Role role) {
        try {
            Role authorizedRole = authFacade.autoLoginSystemTask(role);
            return new Response<>(authorizedRole);
        } catch (Exception e) {
            return new Response<>("Auto-Login Failed: " + e.getMessage());
        }
    }
}