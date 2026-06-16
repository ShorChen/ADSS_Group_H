package presentation.ui_manager;

import context.SessionManager;
import presentation.control.HRManagerShiftController;
import presentation.model.EmployeePL;
import presentation.ui_shared.ShiftsView;
import presentation.ui_shared.ViewCLI;
import presentation.util.Option;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManageShiftsUI extends ViewCLI {
    private final HRManagerShiftController controller;
    private boolean open = false;
    private ShiftsView shiftsView;

    public ManageShiftsUI(Runnable onDismiss) {
        super(onDismiss);
        this.controller = new HRManagerShiftController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            shiftsView = new ShiftsView(0);
            shiftsView.display();
            displayMenu(new Option.Builder("--- Manage Shifts ---")
                    .append("Back", onDismiss)
                    .append("Open Shift", this::openShift)
                    .append("Close Shift", this::closeShift)
                    .append("Place Employee", this::placeEmployees)

            );
        }
    }

    private void openShift() {
        shiftsView.selectClosedShifts(((day, type) -> {
            String id = getNextLine("Enter Shift Manager: ");
            try {
                EmployeePL shiftManager = controller.getShiftManager(id);
                controller.openShift(day, type, shiftManager);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }));
    }

    private void closeShift() {
        shiftsView.selectShift(controller::closeShift);
    }

    private void placeEmployees() {
        LocalDateTime deadline = SessionManager.getDeadline();
        LocalDateTime now = SessionManager.now();

        if (now.isAfter(deadline)) {
            PlaceEmployeesUI placeEmployeesUI = new PlaceEmployeesUI(this::display);
            close();
            placeEmployeesUI.display();
        } else {
            String deadlineString = deadline
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            System.out.println("Placements can only occur after the deadline");
            System.out.println("Come back at " + deadlineString);
        }

    }
    @Override
    public void close() {
        open = false;
    }
}