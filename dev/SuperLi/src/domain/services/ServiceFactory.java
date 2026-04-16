package domain.services;

public class ServiceFactory {
    private final AuthService authService;
    private final EmployeeService employeeService;
    private final EmployeeShiftService employeeShiftService;
    private final HRManagerShiftService managerShiftService;
    private final HistoryService historyService;
    private final RoleService roleService;

    public ServiceFactory(AuthService authService, EmployeeService employeeService,
                          EmployeeShiftService employeeShiftService,
                          HRManagerShiftService managerShiftService,
                          HistoryService historyService, RoleService roleService) {
        this.authService = authService;
        this.employeeService = employeeService;
        this.employeeShiftService = employeeShiftService;
        this.managerShiftService = managerShiftService;
        this.historyService = historyService;
        this.roleService = roleService;
    }
}
