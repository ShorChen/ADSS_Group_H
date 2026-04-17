package presentation.ui;

import context.SessionManager;
import presentation.control.EmployeeShiftController;

import java.util.ArrayList;

public class EmployeeChooseShiftsUI extends View {
    private final ShiftsView shiftsView = new ShiftsView(0);
    private boolean open = false;
    private Runnable onBack;
    private final EmployeeShiftController controller;
    private String menu = """
            ---Options---
            0 - Back to Menu
            1 - Mark Shift as Unavailable
            2 - Submit Availability
            """;

    public EmployeeChooseShiftsUI(Runnable onBack) {
        this.onBack = onBack;
        controller = new EmployeeShiftController();
    }

    @Override
    public void display() {
        open = true;
        while(open) {
            System.out.println("Viewing Schedule of next week");
            shiftsView.display();
            System.out.println("X - Store Is Closed");
            System.out.println("U - Unsaved Changes");
            System.out.println("digit - the amount of employees still needed for the shift (9 means or more)\n");
            System.out.print(menu);

            int selection = getNextInteger("Select option:");

            if (selection == 0) {
                close();
                onBack.run();
            } else if (selection == 1) {

                int day = getNextInteger("Enter day (0=SUN, 1=MON, 2=TUE, 3=WED, 4=THU, 5=FRI, 6=SAT):");
                int shift = getNextInteger("Enter shift (0=DAY, 1=NIGHT):");

                if (day >= 0 && day <= 6 && shift >= 0 && shift <= 1) {
                    char mark = shiftsView.getMark(day, shift);
                    if (mark == ShiftsView.NO_SHIFT)
                        System.out.println("The Store Is Closed For This Shift");
                    else shiftsView.setMarked(day, shift, 'U');

                } else {
                    System.out.println("Invalid selection.");
                }
            } else if (selection == 2) {
                boolean doubles = getNextBoolean("Are you willing to work double shifts? (y/n)");
                controller.setAvailability(
                        SessionManager.getCurrentEmployee().getId(),
                        new ArrayList<>(),
                        doubles);
                System.out.println("Availability Submitted");
                close();
                onBack.run();
            }
        }
    }

    @Override
    public void close() {
        open = false;
    }
}