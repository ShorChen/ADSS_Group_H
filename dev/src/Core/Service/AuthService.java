package Core.Service;

import Core.Domain.AuthFacade;
import Core.Domain.Role;

@SuppressWarnings("ClassCanBeRecord")
public class AuthService {
    private final AuthFacade authFacade;

    public AuthService(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    public Response<Role> login(String code) {
        try {
            Role role = authFacade.login(code);
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
}