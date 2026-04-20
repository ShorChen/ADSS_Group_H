package data_access.pools;

import data_access.entities.ShiftEntity;
import data_access.entities.WeekShiftsEntity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ShiftPool {
    private final Map<LocalDate, WeekShiftsEntity> weeks;

    private static ShiftPool instance;

    public static ShiftPool Instance() {
        if (instance == null)
            instance = new ShiftPool();
        return instance;
    }

    private ShiftPool() {
        this.weeks = new HashMap<>();
    }

    public WeekShiftsEntity getWeek(LocalDate weekDate) {
        if (weeks.containsKey(weekDate))
            return weeks.get(weekDate);
        return null;
    }

    // returns true if @pre(weeks.containsKey(@param(week.getDate)) == true)
    public boolean addWeek(WeekShiftsEntity week) {
        return weeks.put(week.getDate(), week) != null;
    }

    public ShiftEntity getShift(LocalDate weekDate, String day, String shiftType) {
        return getWeek(weekDate).getShift(day, shiftType);
    }

    public void addDayShift(LocalDate weekDate, ShiftEntity shift) {
        WeekShiftsEntity week = getWeek(weekDate);
        if (week == null) {
            week = new WeekShiftsEntity(weekDate, new HashMap<>(), new HashMap<>());
            weeks.put(week.getDate(), week);
        }
        week.addDayShift(shift.getDay(), shift);

    }
    public void addNightShift(LocalDate weekDate, ShiftEntity shift) {
        WeekShiftsEntity week = getWeek(weekDate);
        if (week == null) {
            week = new WeekShiftsEntity(weekDate, new HashMap<>(), new HashMap<>());
            weeks.put(week.getDate(), week);
        }
        week.addNightShift(shift.getDay(), shift);

    }

    public boolean update(ShiftEntity shift){
        return shift.update(shift);
    }
}
