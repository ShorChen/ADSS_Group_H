package presentation.ui_manager;


import presentation.control.HistoryController;
import presentation.ui_shared.ShiftsView;
import presentation.ui_shared.View;
import presentation.util.Option;

public class ViewShiftHistoryUI extends View {
    private boolean open = false;
    private ShiftsView shiftsView;
    private int weeksAgo = 1;

    public ViewShiftHistoryUI(Runnable onDismiss) {
        super(onDismiss);
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
                    .append("Back", onDismiss)
                    .append("View Previous Week", this::loadPreviousWeek)
                    .append("View Next Week", this::loadNextWeek)
                    .append("View Shift", this::viewShift), ""
            );
        }
    }

    private void viewShift() {
        shiftsView.selectDay(shiftsView::displayShift);
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