package Workers.Presentation.Controller;

import Workers.Domain.DTO.RoleSL;
import Workers.Presentation.DTO.BranchPL;
import Workers.Presentation.DTO.EmployeePL;
import Workers.Service.StoreDetailsService;
import Workers.Shared.Enums.WeekDay;

import java.util.List;

public class AddUpdateEmployeeController {
    private final EmployeeController employeeController;
    private final RoleController roleController;
    private final StoreDetailsService storeDetailsService;
    private final BranchController branchController;

    public AddUpdateEmployeeController() {
        employeeController = new EmployeeController();
        roleController = new RoleController();
        storeDetailsService = new StoreDetailsService();
        branchController = new BranchController();
    }

    public List<WeekDay> getClosedDays() {
        return storeDetailsService.getClosedDays();
    }

    public EmployeePL getEmployeeDetails(String employeeId) {
        return employeeController.getEmployeeDetails(employeeId);
    }

    public String addEmployee(EmployeePL employee) {
        return employeeController.addEmployee(employee);
    }

    public boolean updateEmployee(EmployeePL employee, String password) {
        return employeeController.updateEmployee(employee, password);
    }

    public List<String> getNonManagerRoles() {
        List<String> roles = roleController.getAllRoles();
        roles.remove(RoleSL.MANAGER.getTag());
        return roles;
    }

    public List<BranchPL> getAllBranches() {
        return branchController.getBranches();
    }

    public boolean existsEmployeeId(String id) {
        return employeeController.exists(id);
    }
}
