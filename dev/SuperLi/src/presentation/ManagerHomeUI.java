package presentation;

public class ManagerHomeUI extends View {
    public static final StringBuilder managerMenu = new StringBuilder(
            """
                    Actions for manager:
                    0. Logout
                    1. View Shift History
                    2. Manage shifts
                    3. Manage Employees
                    4. Manage Roles
                    """
    );
    private boolean open = false;
    private Runnable onLogout;

    public ManagerHomeUI(Runnable onLogout) {
        this.onLogout = onLogout;
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            System.out.println(managerMenu.toString());
            int selection = getNextInteger("Select option (number)");
            handleSelection(selection,
                    onLogout,
                    this::viewShiftHistory,
                    this::manageShifts,
                    this::manageEmployees,
                    this::manageRoles
            );
        }
    }

    private void manageRoles() {
        ManageRolesUI manageRolesUI = new ManageRolesUI(this::display);
        close();
        manageRolesUI.display();
    }

    private void manageEmployees() {
        ManageEmployeesUI manageEmployeesUI = new ManageEmployeesUI(this::display);
        close();
        manageEmployeesUI.display();
    }

    private void manageShifts() {
        ManageShiftsUI manageShiftsUI = new ManageShiftsUI(this::display);
        close();
        manageShiftsUI.display();
    }

    private void viewShiftHistory() {
        ViewShiftHistoryUI historyUI = new ViewShiftHistoryUI(this::display);
        close();
        historyUI.display();
    }

    @Override
    public void close() {
        open = false;
    }
}