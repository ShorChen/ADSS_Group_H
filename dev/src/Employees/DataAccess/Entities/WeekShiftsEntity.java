package Employees.DataAccess.Entities;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class WeekShiftsEntity {
    private static int ID = 0;
    public static final int NO_ID = -1;
    private final int id;
    private LocalDate date;
    private Map<String, Map<String, ShiftEntity>> shifts; // <day, <shiftType, shift>>

    public WeekShiftsEntity(LocalDate date, Map<String, Map<String, ShiftEntity>> shifts) {
        this(NO_ID, date, shifts);
    }

    public WeekShiftsEntity(int id, @NotNull LocalDate date,
                            @NotNull Map<String,
                                    @NotNull Map<String, ShiftEntity>> shifts) {
        if (id == NO_ID) {
            this.id = ++ID;
        } else this.id = id;
        this.date = date;
        setShifts(shifts);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(@NotNull LocalDate date) {
        this.date = date;
    }

    public ShiftEntity getShift(String day, String shiftType) {
        if (!shifts.containsKey(day)) return null;
        if (!shifts.get(day).containsKey(shiftType)) return null;
        return shifts.get(day).get(shiftType);
    }

    public void addUpdateShift(@NotNull ShiftEntity shift) {
        String day = shift.day();
        String shiftType = shift.shiftType();

        if (!shifts.containsKey(day)) shifts.put(day, new HashMap<>());
        shifts.get(day).put(shiftType, shift);
    }

    public Map<String, Map<String, ShiftEntity>> getShifts() {
        Map<String, Map<String, ShiftEntity>> result = new HashMap<>();
        shifts.forEach((day, oldValue) ->
                result.put(day, new HashMap<>(oldValue)));
        return result;
    }

    public void setShifts(@NotNull Map<String, Map<String, ShiftEntity>> shifts) {
        this.shifts = new HashMap<>();
        shifts.forEach((day, oldValue) ->
                this.shifts.put(day, new HashMap<>(oldValue)));
    }

    public int getId() {
        return id;
    }
}
