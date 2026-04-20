package presentation.ui_manager;


import presentation.control.HistoryController;
import presentation.ui_shared.ShiftsView;
import presentation.ui_shared.View;
import presentation.util.Option;

public class ViewShiftHistoryUI extends View {
    private HistoryController controller;
    private boolean open = false;
    private Runnable onBack;
    private ShiftsView shiftsView;
    private int weeksAgo = 1; // Tracks how far back in history we are looking

    public ViewShiftHistoryUI(Runnable onBack) {
        this.onBack = () -> {
            close();
            onBack.run();
        };
        this.controller = new HistoryController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            System.out.println("\n--- Viewing Shift History: \" + weeksAgo + \" Week(s) Ago ---)");
            shiftsView = new ShiftsView(weeksAgo);
            shiftsView.display();
            System.out.println("X - Store Was Closed");
            System.out.println("P - Partial, Closed Early or Started Late\n");
            displayMenu(new Option.Builder("---Options---")
                    .append("Back", onBack)
                    .append("View Previous Week", this::loadPreviousWeek)
                    .append("View Next Week", this::loadNextWeek)
                    .append("View Shift", this::viewShift), ""
            );
        }
    }

    private void viewShift() {
        int day = getNextInteger("Enter day (0=SUN, 1=MON, 2=TUE, 3=WED, 4=THU, 5=FRI, 6=SAT):");
        int shift = getNextInteger("Enter shift (0=DAY, 1=NIGHT):");


        if (day >= 0 && day <= 6 && shift >= 0 && shift <= 1) {
            char mark = shiftsView.getMark(day, shift);
            if (mark == ShiftsView.NO_SHIFT)
                System.out.println("The Store Was Closed For This Shift");
            else System.out.println(controller.getShiftDetails(day, shift));

        } else {
            System.out.println("Invalid selection.");
        }
    }

    private void loadPreviousWeek() {
        weeksAgo++;
        System.out.println("Loading history for " + weeksAgo + " week(s) ago...");
    }

    private void loadNextWeek() {
        if (weeksAgo > 1) {
            weeksAgo--;
            System.out.println("Loading history for " + weeksAgo + " week(s) ago...");
        } else {
            System.out.println("You are already viewing the most recent history week.");
        }
    }

    @Override
    public void close() {
        open = false;
    }
}