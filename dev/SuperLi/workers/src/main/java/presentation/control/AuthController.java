package presentation.control;

import context.SessionManager;
import domain.entities.Employee;
import domain.services.AuthService;
import presentation.model.EmployeePL;

import java.util.InputMismatchException;

public class AuthController {
    private final AuthService service;

    public AuthController() {
        this.service = new AuthService();
    }

    public boolean registerManager(String id, String password){
        //todo
        return false;
    }
    public boolean login(String id, String password) {
        Employee employee = service.login(id, password);
        if (employee == null) return false;

        SessionManager.login(new EmployeePL(employee));
        return SessionManager.hasContext();
    }

    public void logout() {
        SessionManager.logout();
    }

    public boolean changePassword(String id, String oldPass, String newPass) {
        return service.changePassword(id, oldPass, newPass);
    }

    public boolean isManager(String id){
        return service.isManager(id);
    }

}
