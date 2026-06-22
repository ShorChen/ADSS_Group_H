package Core.Controller;

import Core.Domain.Managers;
import Core.Service.AuthService;
import Core.Service.Response;

@SuppressWarnings({"ClassCanBeRecord", "unused"})
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public Managers login(String username) throws Exception {
        Response<Managers> response = authService.login(username);
        if (!response.isSuccess()) throw new Exception(response.getErrorMessage());
        return response.getData();
    }

    public void logout() {
        Response<Boolean> response = authService.logout();
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
    }
}