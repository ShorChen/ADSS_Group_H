package data_access.pools;

import data_access.entities.ShiftEntity;
import data_access.entities.WeekShiftsEntity;
import util.BoundedSet;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class ShiftPool {
    private final Map<LocalDate, WeekShiftsEntity> weeks;
    private List<String> closedDays;
    private static ShiftPool instance;

    public static ShiftPool Instance() {
        if (instance == null)
            instance = new ShiftPool();
        return instance;
    }

    private ShiftPool() {
        this.weeks = new HashMap<>();
        this.closedDays = new ArrayList<>();

        WeekShiftsEntity week = new WeekShiftsEntity(
                LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                , new HashMap<>(), new HashMap<>()
        );

        Map<String, Set<String>> employees = new HashMap<>();
        Set<String> shiftManager = new BoundedSet<>(1);
        shiftManager.add("<SHIFT MANAGER ID>");
        employees.put("Shift Manager", shiftManager);
        week.addDayShift("SUNDAY", new ShiftEntity(null, "SUNDAY",
                "DAY", employees, new HashMap<>()));

        week.addNightShift("SUNDAY", new ShiftEntity(null, "SUNDAY",
                "EVENING", employees, new HashMap<>()));


        weeks.put(week.getDate(), week);

    }

    public List<String> getClosedDays() {
        return closedDays;
    }

    public void setClosedDays(List<String> closedDays) {
        this.closedDays = closedDays;
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

    public void clear() {
        if (this.weeks != null) {
            this.weeks.clear();
        }
        if (this.closedDays != null) {
            this.closedDays.clear();
        }
    }

    public boolean update(ShiftEntity shift) {
        return shift.update(shift);
    }

    public void updateWeek(WeekShiftsEntity entity) {
        weeks.put(entity.getDate(), entity);
    }
}
