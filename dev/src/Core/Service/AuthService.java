package Core.Service;

import Core.Domain.AuthFacade;
import Core.Domain.Managers;

@SuppressWarnings("ClassCanBeRecord")
public class AuthService {
    private final AuthFacade authFacade;

    public AuthService(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    public Response<Managers> login(String code) {
        try {
            Managers managers = authFacade.login(code);
            return new Response<>(managers);
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
    public Response<Managers> getCurrentRole() {
        try {
            return new Response<>(authFacade.getCurrentRole());
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }
}