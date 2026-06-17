package Employees.Presentation.Controller;

import Employees.Context.SessionManager;
import Employees.Domain.Entities.Employee;
import Employees.Domain.Service.AuthService;
import Employees.Presentation.Model.EmployeePL;

public class AuthController {
    private final AuthService service;

    public AuthController() {
        this.service = new AuthService();
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

    public boolean changeCurrentEmployeePassword(String oldPass, String newPass) {
        return service.changePassword(SessionManager.getCurrentEmployee().getId(), oldPass, newPass);
    }

    public boolean isManager(String id){
        return service.isManager(id);
    }

}
