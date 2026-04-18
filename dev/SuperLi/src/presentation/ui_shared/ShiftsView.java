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
    private final StringBuilder shifts = new StringBuilder(
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

    private int getIndexOf(int day, int type) {
        return controller.getIndexOf(day, type);
    }

    public void setMarked(int day, int type, char c) {
        shifts.setCharAt(getIndexOf(day, type), c);
    }

    public char getMark(int day, int type) {
        return shifts.charAt(getIndexOf(day, type));
    }


    @Override
    public void display() {
        System.out.print(shifts);
    }

    @Override
    public void close() {

    }
}
