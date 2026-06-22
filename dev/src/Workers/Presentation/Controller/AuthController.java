package Workers.Presentation.Controller;

import Workers.Context.SessionManager;
import Workers.Domain.DTO.EmployeeSL;
import Workers.Service.AuthService;
import Workers.Presentation.DTO.EmployeePL;

public class AuthController {
    private final AuthService service;

    public AuthController() {
        this.service = new AuthService();
    }

    public boolean login(String id, String password) {
        EmployeeSL employee = service.login(id, password);
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

    public boolean isManager(String id) {
        return service.isManager(id);
    }

    public boolean isBranchManager(String id) {
        return service.isBranchManager(id);
    }

    public void selectCurrentEmployeeBranch() {
        SessionManager.setSelectedBranchId(SessionManager.getCurrentEmployee().getBranchId());
    }
}
