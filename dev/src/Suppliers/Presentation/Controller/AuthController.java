package Suppliers.Presentation.Controller;

import Suppliers.Domain.Security.Role;
import Suppliers.Service.Core.AuthService;
import Suppliers.Service.Response;

public class AuthController {
    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    public String login(String code) throws Exception {
        Response<Boolean> loginResponse = authService.login(code);
        if (loginResponse.isSuccess()) {
            Response<Role> roleResponse = authService.getCurrentRole();
            if (roleResponse.isSuccess())
                return roleResponse.getData().name();
        }
        throw new Exception(loginResponse.getErrorMessage());
    }

    public boolean logout() throws Exception {
        Response<Boolean> response = authService.logout();
        if (response.isSuccess()) return response.getData();
        throw new Exception(response.getErrorMessage());
    }
}