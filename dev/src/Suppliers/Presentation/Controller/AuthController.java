package Suppliers.Presentation.Controller;

import Suppliers.Domain.Service.AuthService;
import Suppliers.Domain.Service.Response;

public class AuthController {
    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    public boolean login(String code) throws Exception {
        Response<Boolean> response = authService.login(code);
        if (response.isSuccess()) return response.getData();
        throw new Exception(response.getErrorMessage());
    }

    public boolean logout() throws Exception {
        Response<Boolean> response = authService.logout();
        if (response.isSuccess()) return response.getData();
        throw new Exception(response.getErrorMessage());
    }
}