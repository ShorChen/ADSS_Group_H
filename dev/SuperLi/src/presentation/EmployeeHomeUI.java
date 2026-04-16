package presentation;
import presentation.controllers.ShiftController;

public class EmployeeHomeUI extends View {
    private boolean open = false;
    private final Runnable onLogout;
    private final ShiftController shiftController;

    private static final StringBuilder menu = new StringBuilder(
            """
                    --- Employee Main Menu ---
                    0. Logout
                    1. Manage My Shifts
                    """
    );

    public EmployeeHomeUI(Runnable onLogout, ShiftController shiftController) {
        this.onLogout = () -> {
            close();
            onLogout.run();
        };
        this.shiftController = shiftController;
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            System.out.print(menu.toString());
            int selection = getNextInteger("Select an option (number):");

            handleSelection(selection,
                    onLogout,
                    this::openChooseShifts
            );
        }
    }

    private void openChooseShifts() {
        EmployeeChooseShiftsUI ui = new EmployeeChooseShiftsUI(
                () -> System.out.println("Returning to Main Menu..."), 
                shiftController
        );
        ui.display();
    }

    @Override
    public void close() {
        open = false;
    }
}