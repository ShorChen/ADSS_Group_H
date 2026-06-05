package Core.Controller;

import Core.Domain.Role;
import Core.Service.AuthService;
import Core.Service.Response;

@SuppressWarnings("ClassCanBeRecord")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public Role login(String username) throws Exception {
        Response<Role> response = authService.login(username);
        if (!response.isSuccess()) throw new Exception(response.getErrorMessage());
        return response.getData();
    }

    public void logout() {
        Response<Boolean> response = authService.logout();
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
    }
}