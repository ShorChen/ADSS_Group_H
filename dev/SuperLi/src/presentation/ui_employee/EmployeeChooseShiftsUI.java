package presentation.ui_employee;

import context.SessionManager;
import presentation.control.EmployeeShiftController;
import presentation.ui_shared.ShiftsView;
import presentation.ui_shared.View;
import presentation.util.Option;

import java.util.*;

public class EmployeeChooseShiftsUI extends View {
    private final ShiftsView shiftsView = new ShiftsView(0);
    private boolean open = false;
    private final Runnable onBack;
    private final EmployeeShiftController controller;
    Map<Integer, Set<Integer>> selectedShifts;

    public EmployeeChooseShiftsUI(Runnable onBack) {
        this.onBack = onBack;
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

            displayMenu("---Options---", "",
                    new Option("Back (Cancels Changes)", onBack),
                    new Option("Mark Shift as Unavailable", this::markShift),
                    new Option("Submit Availability", this::submit)
            );
        }
    }

    private void markShift() {
        int day = getNextInteger("Enter day (0=SUN, 1=MON, 2=TUE, 3=WED, 4=THU, 5=FRI, 6=SAT):");
        int shift = getNextInteger("Enter shift (0=DAY, 1=NIGHT):");

        if (day >= 0 && day <= 6 && shift >= 0 && shift <= 1) {
            char mark = shiftsView.getMark(day, shift);
            if (mark == ShiftsView.NO_SHIFT)
                System.out.println("The Store Is Closed For This Shift");
            else {
                shiftsView.setMarked(day, shift, 'U');
                addSelectedShift(selectedShifts, day, shift);
            }

        } else {
            System.out.println("Invalid selection.");
        }
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