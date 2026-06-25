package Core.Controller;

import Core.Domain.Role;
import Core.Service.AuthService;
import Core.Service.Response;

@SuppressWarnings({"ClassCanBeRecord"})
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public Role login(String id, String password) {
        Response<Role> response = authService.login(id, password);
        if (!response.isSuccess())
            throw new RuntimeException(response.getErrorMessage());
        return response.getData();
    }

    public void logout() {
        Response<Boolean> response = authService.logout();
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
    }

    @SuppressWarnings("UnusedReturnValue")
    public Role autoLoginSystemTask(Role role) {
        Response<Role> response = authService.autoLoginSystemTask(role);
        if (!response.isSuccess())
            throw new RuntimeException(response.getErrorMessage());
        return response.getData();
    }
}