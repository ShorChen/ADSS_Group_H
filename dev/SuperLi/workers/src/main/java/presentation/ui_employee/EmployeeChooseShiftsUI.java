package presentation.ui_employee;

import context.SessionManager;
import presentation.control.employee.EmployeeShiftController;
import presentation.ui_shared.ShiftsView;
import presentation.ui_shared.View;
import presentation.util.Option;

import java.util.*;

public class EmployeeChooseShiftsUI extends View {
    private final ShiftsView shiftsView = new ShiftsView(0);
    private boolean open = false;
    private final EmployeeShiftController controller;
    Map<Integer, Set<Integer>> selectedShifts;

    public EmployeeChooseShiftsUI(Runnable onDismiss) {
        super(onDismiss);
        controller = new EmployeeShiftController();
    }

    @Override
    public void display() {
        open = true;
        selectedShifts = new HashMap<>();
        while (open) {
            System.out.println("Viewing Schedule of next week");
            shiftsView.display();
            System.out.println("X - Store Is Closed");
            System.out.println("U - Unsaved Changes");

            displayMenu(new Option.Builder("---Options---")
                    .append("Back (Cancels Changes)", onDismiss)
                    .append("Mark Shift as Unavailable", this::markShift)
                    .append("View Placement", this::viewShifts)
                    .append("Submit Availability", this::submit), "");
        }
    }

    private void viewShifts() {
        shiftsView.selectDay(shiftsView::displayShift);
    }

    private void markShift() {
        shiftsView.selectDay(((day, shift) -> {
            shiftsView.setMarked(day, shift, 'U');
            addSelectedShift(selectedShifts, day, shift);
        }));
    }

    private void submit() {
        boolean doubles = getNextBoolean("Are you willing to work double shifts? (y/n)");
        controller.setAvailability(
                SessionManager.getCurrentEmployee().getId(),
                selectedShifts,
                doubles);
        System.out.println(selectedShifts);
        System.out.println("Availability Submitted");
    }

    private void addSelectedShift(Map<Integer, Set<Integer>> map, int key, int value) {
        if (map.containsKey(key))
            map.get(key).add(value);
        Set<Integer> values = new HashSet<>();
        values.add(value);
        map.put(key, values);
    }

    @Override
    public void close() {
        open = false;
    }
}