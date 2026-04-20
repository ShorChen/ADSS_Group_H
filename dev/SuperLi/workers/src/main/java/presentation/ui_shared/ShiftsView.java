package presentation.ui_shared;

import presentation.control.shared.ShiftViewController;
import presentation.model.EmployeePL;
import presentation.model.ShiftPL;

import java.util.Map;

public class ShiftsView extends View {
    private static final int INDEX_SUN_DAY = 116;
    private static final int INDEX_SUN_NIGHT = 188;
    private static final int MARGIN = 4;
    public static Character NO_SHIFT = 'X';
    public static Character PARTIAL_SHIFT = 'P';
    private final ShiftViewController controller;
    private final Map<Integer, Map<Integer, ShiftPL>> weekShifts;
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
        weekShifts = controller.getWeek(weeksAgo);
        weekShifts.forEach((day, shifts) -> {
            setMarked(day, 0,
                    shifts.get(0) == null ? NO_SHIFT : ' ');
            setMarked(day, 1,
                    shifts.get(1) == null ? NO_SHIFT : ' ');
        });
    }

    public void selectDay(OnShiftSelect onSelect) {
        int day = getNextInteger("Enter day (0=SUN, 1=MON, 2=TUE, 3=WED, 4=THU, 5=FRI, 6=SAT):");
        int shift = getNextInteger("Enter shift (0=DAY, 1=NIGHT):");

        if (day >= 0 && day <= 6 && shift >= 0 && shift <= 1) {
            char mark = getMark(day, shift);
            if (mark == ShiftsView.NO_SHIFT)
                System.out.println("The Store Is Closed For This Shift");
            else if (onSelect != null) onSelect.run(day, shift);
        } else {
            System.out.println("Invalid selection.");
        }
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


        shift.getEmployees().forEach((role, workers) -> {
            workers.forEach(employee -> {
                EmployeePL employeePL = controller.getEmployeeDetails(employee);
                stringBuilder.append("Name: ").append(employeePL.getName())
                        .append(", Role: ").append(role);
            });
        });

        System.out.println(stringBuilder);
    }

    public void closeShift(int day, int type) {
        setMarked(day, type, NO_SHIFT);
    }

    public void openShift(int day, int type) {
        setMarked(day, type, ' ');
    }


    public interface OnShiftSelect {
        void run(int day, int type);
    }

    private int getIndexOf(int day, int type) {
        return day * MARGIN + INDEX_SUN_DAY +
               (INDEX_SUN_NIGHT - INDEX_SUN_DAY) * type;
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
