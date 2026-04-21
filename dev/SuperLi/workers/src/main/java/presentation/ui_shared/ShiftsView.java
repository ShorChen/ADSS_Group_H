package presentation.ui_shared;

import domain.enums.ShiftType;
import domain.enums.WeekDay;
import presentation.control.ShiftViewController;
import presentation.model.EmployeePL;
import presentation.model.ShiftPL;
import presentation.util.Option;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class ShiftsView extends View {
    private static final int INDEX_SUN_DAY = 116;
    private static final int INDEX_SUN_NIGHT = 188;
    private static final int MARGIN = 4;
    public static Character NO_SHIFT = 'X';
    public static Character PARTIAL_SHIFT = 'P';
    private final ShiftViewController controller;
    private Map<Integer, Map<Integer, ShiftPL>> weekShifts;
    private final Function<ShiftPL, Boolean> predicate;
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
        super(null);
        controller = new ShiftViewController();
        predicate = Objects::nonNull;
        init(weeksAgo);
    }

    public ShiftsView(int weeksAgo, Function<ShiftPL, Boolean> predicate) {
        super(null);
        controller = new ShiftViewController();
        this.predicate = predicate;
        init(weeksAgo);
    }


    private void init(int weeksAgo) {
        weekShifts = controller.getWeek(weeksAgo);
        weekShifts.forEach((day, shifts) -> {

            ShiftPL s = shifts.get(0);
            setMarked(day, 0,
                    predicate.apply(s) ? ' ' : NO_SHIFT);
            //s == null ? NO_SHIFT : predicate.apply(s) ? 'V' : ' ');

            s = shifts.get(1);
            setMarked(day, 1,
                    predicate.apply(s) ? ' ' : NO_SHIFT);
            //s == null ? NO_SHIFT : predicate.apply(s) ? 'V' : ' ');
        });
    }

    private void selectShift(Function<ShiftPL, Boolean> pred,
                             OnShiftSelect onSelect) {
        int[] day = new int[1];
        int[] type = new int[1];

        final Option.Builder builderDays = new Option.Builder("Enter Shift's Week Day");
        weekShifts.forEach((weekDayOrdinal, shifts) -> {
            boolean[] foundShift = {false};
            shifts.forEach((_, shiftPL) -> {
                if (pred.apply(shiftPL)) foundShift[0] = true;
            });

            if (foundShift[0])
                builderDays.append(WeekDay.values()[weekDayOrdinal].day, () ->
                        day[0] = weekDayOrdinal);

        });
        if (builderDays.size() == 0) {
            System.out.println("Found no matching shifts for this week");
            return;
        }

        displayMenu(builderDays, "");

        final Option.Builder builderShifts = new Option.Builder("Enter Shift's Type");
        weekShifts.get(day[0]).forEach((shiftTypeOrdinal, shiftPL) -> {
            if (pred.apply(shiftPL)) {
                builderShifts.append(ShiftType.values()[shiftTypeOrdinal].type, () ->
                        type[0] = shiftTypeOrdinal);
            }
        });

        if (builderShifts.size() == 0) {
            System.out.println("Found no matching shifts for this day");
            return;
        }

        displayMenu(builderShifts, "");
        if (onSelect != null) onSelect.run(day[0], type[0]);
    }


    public void selectShiftOfPredicate(OnShiftSelect onSelect) {
        selectShift(predicate, onSelect);
    }

    public void selectShift(OnShiftSelect onSelect) {
        selectShift(Objects::nonNull, onSelect);
    }

    public void selectClosedShifts(OnShiftSelect onSelect) {
        selectShift(Objects::isNull, onSelect);
    }


    public void displayShift(int day, int type) {
        Map<Integer, ShiftPL> shiftsOfDay = weekShifts.getOrDefault(day, null);
        if (shiftsOfDay == null) System.out.println("Day does not have shifts");
        else {
            ShiftPL shift = shiftsOfDay.getOrDefault(type, null);
            if (shift == null) System.out.println("No shift found");
            else printDisplayedShift(shift);
        }
    }

    private void printDisplayedShift(ShiftPL shift) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Shift Start Date: ").append(shift.getStartDate()).append("\n")
                .append("Day: ").append(shift.getDay().day).append("\n")
                .append("Shift Type: ").append(shift.getShiftType().type).append("\n");


        if (!shift.getEmployees().isEmpty())
            stringBuilder.append("---Employees---\n");

        shift.getEmployees().forEach((role, workers) -> {
            workers.forEach(employee -> {
                EmployeePL employeePL = controller.getEmployeeDetails(employee);
                stringBuilder.append(" ,Id: ").append(employeePL.getId())
                        .append(", Role: ").append(role).append("\n");
            });
        });

        System.out.println(stringBuilder);
    }


    public void setUnsaved(int day, int shift) {
        setMarked(day, shift, 'U');
    }


    public interface OnShiftSelect {
        void run(int day, int type);
    }

    private int getIndexOf(int day, int type) {
        return day * MARGIN + INDEX_SUN_DAY +
               (INDEX_SUN_NIGHT - INDEX_SUN_DAY) * type;
    }

    private void setMarked(int day, int type, char c) {
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
