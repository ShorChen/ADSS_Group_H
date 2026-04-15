package presentation;


import domain.enums.ShiftType;

public class ShiftsView extends View{
    static final int INDEX_SUN_DAY = 116;
    static final int INDEX_SUN_NIGHT = 188;
    static final int MARGIN = 4;
    static StringBuilder shifts = new StringBuilder(
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

    private static int getIndexOf(WeekDay day, ShiftType type) {
        int i = type == ShiftType.DAY ? 0 : 1;
        return day.day * MARGIN + INDEX_SUN_DAY +
                (INDEX_SUN_NIGHT - INDEX_SUN_DAY) * i;
    }

    public static void setMarked(WeekDay day, ShiftType type, char c) {
        shifts.setCharAt(getIndexOf(day, type), c);
        System.out.println(shifts.toString());
    }

    @Override
    public void display() {
        System.out.println(shifts.toString());
    }

    @Override
    public void close() {

    }
}
