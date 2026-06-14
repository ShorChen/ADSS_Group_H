package presentation.ui_employee;

import context.SessionManager;
import domain.entities.ShiftKey;
import presentation.control.EmployeeShiftController;
import presentation.ui_shared.ShiftsView;
import presentation.ui_shared.ViewCLI;
import presentation.util.Option;
import shared.enums.ShiftType;
import shared.enums.WeekDay;

import java.util.HashSet;
import java.util.Set;

public class EmployeeChooseShiftsUI extends ViewCLI {
    private final ShiftsView shiftsView = new ShiftsView(0);
    private boolean open = false;
    private final EmployeeShiftController controller;
    private Set<ShiftKey> selectedShifts;

    public EmployeeChooseShiftsUI(Runnable onDismiss) {
        super(onDismiss);
        controller = new EmployeeShiftController();
    }

    @Override
    public void display() {
        open = true;
        selectedShifts = new HashSet<>();
        while (open) {
            System.out.println("Viewing Schedule of next week");
            shiftsView.display();
            System.out.println("X - Store Is Closed");
            System.out.println("U - Unsaved Changes");

            displayMenu(new Option.Builder("---Options---")
                    .append("Back (Cancels Changes)", onDismiss)
                    .append("Mark Shift as Unavailable", this::markShift)
                    .append("View Placement", this::viewShifts)
                    .append("Submit Availability", this::submit));
        }
    }

    private void viewShifts() {
        shiftsView.selectShift(shiftsView::displayShift);
    }

    private void markShift() {
        shiftsView.selectShift(((day, shift) -> {
            shiftsView.setUnsaved(day, shift);
            addSelectedShift(day, shift);
        }));
    }

    private void submit() {
        boolean isWorkingDoubles = getNextBoolean("Are you willing to work double shifts? (y/n)");
        controller.setCurrentEmployeeShiftsAsUnavailable(selectedShifts, isWorkingDoubles);
        System.out.println("Availability Submitted");
        System.out.println("You can re-submit your availability again until the deadline: "
                           + SessionManager.getDeadline());
        onDismiss.run();
    }

    private void addSelectedShift(WeekDay day, ShiftType shift) {
        selectedShifts.add(new ShiftKey(day, shift));
    }

    @Override
    public void close() {
        open = false;
    }
}