package presentation; // שייכי לתיקייה הנכונה אצלך (presentation או presentation.cli)

import presentation.controllers.EmployeeController;
import presentation.controllers.RoleController;
import presentation.controllers.ShiftController;

public class ManagerHomeUI extends View {
    private boolean open = false;
    private final Runnable onLogout;
    
    private final EmployeeController employeeController;
    private final ShiftController shiftController;
    private final RoleController roleController;

    private static final StringBuilder menu = new StringBuilder(
            """
                    --- Manager Main Menu ---
                    0. Logout
                    1. Manage Employees
                    2. Manage Shifts (Schedule)
                    3. Manage Roles
                    """
    );

    public ManagerHomeUI(Runnable onLogout, EmployeeController empCtrl, ShiftController shiftCtrl, RoleController roleCtrl) {
        this.onLogout = () -> {
            close();
            onLogout.run();
        };
        this.employeeController = empCtrl;
        this.shiftController = shiftCtrl;
        this.roleController = roleCtrl;
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            System.out.print(menu.toString());
            int selection = getNextInteger("Select an option (number):");

            handleSelection(selection,
                    onLogout,
                    this::openManageEmployees,
                    this::openManageShifts,
                    this::openManageRoles
            );
        }
    }

    private void openManageEmployees() {
        ManageEmployeesUI ui = new ManageEmployeesUI(() -> System.out.println("Returning to Main Menu..."), employeeController);
        ui.display();
    }

    private void openManageShifts() {
        ManageShiftsUI ui = new ManageShiftsUI(() -> System.out.println("Returning to Main Menu..."), shiftController);
        ui.display();
    }

    private void openManageRoles() {
        ManageRolesUI ui = new ManageRolesUI(() -> System.out.println("Returning to Main Menu..."), roleController);
        ui.display();
    }

    @Override
    public void close() {
        open = false;
    }
}