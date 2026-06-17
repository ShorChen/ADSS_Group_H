package Employees.Presentation.UIShared;

import Employees.Domain.Entities.ShiftKey;
import Employees.Presentation.Controller.ShiftViewController;
import Employees.Presentation.Model.EmployeePL;
import Employees.Presentation.Model.ShiftPL;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;

import java.util.*;
import java.util.function.Function;

public class ShiftsView extends ViewCLI {
    private static final int INDEX_SUN_DAY = 116;
    private static final int INDEX_SUN_NIGHT = 188;
    private static final int MARGIN = 4;
    public static Character NO_SHIFT = 'X';
    public static Character PARTIAL_SHIFT = 'P';
    private final ShiftViewController controller;
    private Map<ShiftKey, ShiftPL> weekShifts;
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
        weekShifts.forEach((key, shift) ->
                setMarked(key.day(), key.shiftType(), predicate.apply(shift) ? ' ' : NO_SHIFT));
    }

    private void selectShift(Function<ShiftPL, Boolean> pred,
                             OnShiftSelect onSelect) {
        Map<ShiftKey, ShiftPL> filteredMap = new HashMap<>();
        weekShifts.forEach((key, value) -> {
            if (pred.apply(value)) filteredMap.put(key, value);
        });

        List<WeekDay> days = new ArrayList<>();
        filteredMap.forEach((key, _) -> {
            if (!days.contains(key.day())) days.add(key.day());
        });

        if (!days.isEmpty()) {
            WeekDay day = selectionMenuOf("Enter Shift's Week Day", days);
            List<ShiftType> shifts = new ArrayList<>();
            filteredMap.forEach((key, _) -> {
                if (key.day() == day) shifts.add(key.shiftType());
            });
            ShiftType type = selectionMenuOf("Enter Shift's Type", shifts);
            if (onSelect != null) onSelect.run(day, type);
        } else {
            System.out.println("Found no matching shifts for this week");
        }

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


    public void displayShift(WeekDay day, ShiftType type) {
        ShiftPL shift = weekShifts.getOrDefault(new ShiftKey(day, type), null);
        if (shift == null) System.out.println("No Shift Found");
        else printDisplayedShift(shift);
    }

    private void printDisplayedShift(ShiftPL shift) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Shift Start Date: ").append(shift.getStartDate()).append("\n")
                .append("Day: ").append(shift.getDay().day).append("\n")
                .append("Shift Type: ").append(shift.getShiftType().type).append("\n");


        if (!shift.getEmployees().isEmpty())
            stringBuilder.append("---Employees---\n");

        shift.getEmployees().forEach((role, workers) ->
                workers.forEach(employee -> {
                    EmployeePL employeePL = controller.getEmployeeDetails(employee);
                    stringBuilder.append(" ,Id: ").append(employeePL.getId())
                            .append(", Role: ").append(role).append("\n");
                }));

        System.out.println(stringBuilder);
    }


    public void setUnsaved(WeekDay day, ShiftType shift) {
        setMarked(day, shift, 'U');
    }


    public interface OnShiftSelect {
        void run(WeekDay day, ShiftType type);
    }

    private int getIndexOf(WeekDay day, ShiftType type) {

        int ordDay = day.ordinal();
        int ordType = type.ordinal();

        return ordDay * MARGIN + INDEX_SUN_DAY +
               (INDEX_SUN_NIGHT - INDEX_SUN_DAY) * ordType;
    }

    private void setMarked(WeekDay day, ShiftType type, char c) {
        calendar.setCharAt(getIndexOf(day, type), c);
    }

    public char getMark(WeekDay day, ShiftType type) {
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
