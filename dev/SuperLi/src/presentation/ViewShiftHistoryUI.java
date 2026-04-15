package presentation;

import service.services.HistoryService;

public class ViewShiftHistoryUI extends View {
    private ShiftsView shiftsView;
    private HistoryService service;
    private boolean open = false;
    private Runnable onBack;
    private int weeksAgo = 1; // Tracks how far back in history we are looking

    private static final StringBuilder historyMenu = new StringBuilder(
            """
                    0. Back
                    1. View Previous Week
                    2. View Next Week
                    """
    );

    public ViewShiftHistoryUI(Runnable onBack) {
        this.onBack = onBack;
        this.shiftsView = new ShiftsView();
        this.service = new HistoryService();
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            System.out.println("\n--- Viewing Shift History: " + weeksAgo + " Week(s) Ago ---");

            // In a real implementation, you would fetch the history for the selected week
            // from the HistoryService and map it onto the ShiftsView before displaying.
            shiftsView.display();

            System.out.println(historyMenu.toString());
            int selection = getNextInteger("Select option (number):");

            handleSelection(selection,
                    () -> { close(); onBack.run(); },
                    this::loadPreviousWeek,
                    this::loadNextWeek
            );
        }
    }

    private void loadPreviousWeek() {
        weeksAgo++;
        System.out.println("Loading history for " + weeksAgo + " week(s) ago...");
        // TODO: Call HistoryService to get older shifts and update ShiftsView
    }

    private void loadNextWeek() {
        if (weeksAgo > 1) {
            weeksAgo--;
            System.out.println("Loading history for " + weeksAgo + " week(s) ago...");
            // TODO: Call HistoryService to get newer shifts and update ShiftsView
        } else {
            System.out.println("You are already viewing the most recent history week.");
        }
    }

    @Override
    public void close() {
        open = false;
    }
}