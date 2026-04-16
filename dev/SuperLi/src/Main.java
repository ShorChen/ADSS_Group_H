import data_access.pools.*;
import domain.entities.Employee;
import domain.entities.Role;
import domain.services.*;
import presentation.*;
import presentation.controllers.*;
import presentation.ManagerHomeUI;
import presentation.EmployeeHomeUI;

public class Main {
    public static void main(String[] args) {
        
        AuthPool authPool = new AuthPool();
        EmployeePool employeePool = new EmployeePool();
        ShiftPool shiftPool = new ShiftPool();
        RolePool rolePool = new RolePool();

        AuthService authService = new AuthService(authPool);
        EmployeeService employeeService = new EmployeeService(employeePool);
        HRManagerShiftService hrShiftService = new HRManagerShiftService(shiftPool, employeePool);
        EmployeeShiftService employeeShiftService = new EmployeeShiftService(shiftPool);
        RoleService roleService = new RoleService(rolePool);

        AuthController authController = new AuthController(authService);
        EmployeeController employeeController = new EmployeeController(employeeService);
        ShiftController shiftController = new ShiftController(hrShiftService, employeeShiftService);
        RoleController roleController = new RoleController(roleService);

        System.out.println("=== System Initializing ===");
        
        Employee manager = new Employee("000000000");
        manager.addRole(Role.MANAGER); 
        employeePool.addEmployee(manager);
        String managerPass = authService.addWorker(manager);
        System.out.println("Created Manager -> ID: 000000000 | Initial Password: " + managerPass);

        Employee worker = new Employee("111111111");
        employeePool.addEmployee(worker);
        String workerPass = authService.addWorker(worker);
        System.out.println("Created Worker  -> ID: 111111111 | Initial Password: " + workerPass);
        
        System.out.println("===========================\n");

        LoginUI.CloseLoginUI onLoginAction = (id, pass, isManager) -> {
            if (isManager) {
                ManagerHomeUI managerHomeUI = new ManagerHomeUI(
                        () -> System.out.println("Logged out successfully.\n"), 
                        employeeController, 
                        shiftController, 
                        roleController
                );
                managerHomeUI.display();
            } else {
                EmployeeHomeUI employeeHomeUI = new EmployeeHomeUI(
                        () -> System.out.println("Logged out successfully.\n"),
                        shiftController
                );
                employeeHomeUI.display();
            }
        };

        LoginUI loginUI = new LoginUI(onLoginAction, authController);
        loginUI.display();
    }
}