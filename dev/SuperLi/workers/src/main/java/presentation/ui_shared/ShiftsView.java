package presentation.ui_shared;

import presentation.control.ShiftController;

import java.util.Map;

public class ShiftsView extends View {
    private static final int INDEX_SUN_DAY = 116;
    private static final int INDEX_SUN_NIGHT = 188;
    private static final int MARGIN = 4;
    public static Character NO_SHIFT = 'X';
    public static Character PARTIAL_SHIFT = 'P';
    private final ShiftController controller;
    private final StringBuilder calendar = new StringBuilder(
            """
                    +-----+---+---+---+---+---+---+---+
                    |Shift|Sun|Mon|Tue|Wed|Thu|Fri|Sat|
                    +-----+---+---+---+---+---+---+---+
                    | day |   |   |   |   |   |   |   |
                    +-----+---+---+---+---+---+---+---|
                    |night|   |   |   |   |   |   |   |
                    +-----+---+---+---+---+---+---+---+
                    """
    );

    public ShiftsView(int weeksAgo) {
        controller = new ShiftController();
        Map<Integer, Map<Integer, Character>> shifts = controller.getNWeeksAgo(weeksAgo);
        shifts.forEach((day, chars) -> {
            setMarked(day, 0, chars.get(0));
            setMarked(day, 1, chars.get(1));
        });
    }

    private void markShift(Runnable onSuccess) {
        int day = getNextInteger("Enter day (0=SUN, 1=MON, 2=TUE, 3=WED, 4=THU, 5=FRI, 6=SAT):");
        int shift = getNextInteger("Enter shift (0=DAY, 1=NIGHT):");

        if (day >= 0 && day <= 6 && shift >= 0 && shift <= 1) {
            char mark = getMark(day, shift);
            if (mark == ShiftsView.NO_SHIFT)
                System.out.println("The Store Is Closed For This Shift");
            else onSuccess.run();
        } else {
            System.out.println("Invalid selection.");
        }
    }

    private int getIndexOf(int day, int type) {
        return controller.getIndexOf(day, type);
    }

    public void setMarked(int day, int type, char c) {
        calendar.setCharAt(getIndexOf(day, type), c);
    }

    public char getMark(int day, int type) {
        return calendar.charAt(getIndexOf(day, type));
    }


    @Override
    public void display() {
        System.out.print(calendar);
    }

    @Override
    public void close() {

    }
}
