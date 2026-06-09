package data_access.pools;

import data_access.entities.ShiftEntity;
import data_access.entities.keys.BranchWeekKey;
import domain.entities.Shift;
import org.jetbrains.annotations.NotNull;
import shared.enums.ShiftType;
import shared.enums.WeekDay;

import java.util.*;

public class ShiftPool {
    private final Map<BranchWeekKey, List<ShiftEntity>> shiftsTable;
    private static ShiftPool instance;

    public static ShiftPool Instance() {
        if (instance == null)
            instance = new ShiftPool();
        return instance;
    }

    private ShiftPool() {
        this.shiftsTable = new HashMap<>();
    }

//    @Deprecated
//    public WeekShiftsEntity getWeek(int weekId) {
//        if (weeks.containsKey(weekId))
//            return weeks.get(weekId);
//        WeekShiftsEntity entity = new WeekShiftsEntity(weekId, new HashMap<>());
//        weeks.put(weekId, entity);
//        return entity;
//    }

//    @Deprecated
//    public boolean exists(@NotNull WeekShiftsEntity week) {
//        return weeks.containsKey(week.getDate());
//    }

//    public ShiftEntity getShift(LocalDate weekDate, String day, String shiftType) {
//        return getShiftsByBranchAndWeek().getShift(day, shiftType);
//    }

//    public void addDayShift(LocalDate weekDate, ShiftEntity shift) {
//        WeekShiftsEntity week = getWeek(weekDate);
//        if (week == null) {
//            week = new WeekShiftsEntity(weekDate, new HashMap<>());
//            weeks.put(week.getDate(), week);
//        }
//        week.addUpdateShift(shift);
//
//    }

//    public void addNightShift(LocalDate weekDate, ShiftEntity shift) {
//        WeekShiftsEntity week = getWeek(weekDate);
//        if (week == null) {
//            week = new WeekShiftsEntity(weekDate, new HashMap<>());
//            weeks.put(week.getDate(), week);
//        }
//        week.addUpdateShift(shift);
//
//    }

    public void clear() {
        shiftsTable.clear();
    }

//    public void addUpdateShift(LocalDate weekDate, ShiftEntity shift) {
//        WeekShiftsEntity week = getWeek(weekDate);
//        if (week == null) {
//            week = new WeekShiftsEntity(weekDate, new HashMap<>());
//            weeks.put(week.getDate(), week);
//        }
//        week.addUpdateShift(shift);
//    }

//    public boolean update(ShiftEntity shift) {
//
//        //        return employees.getEmployee(employee.getId()).update(employee.toEntity(password));
//        return shift.update(shift);
//    }

//    public void addUpdateWeek(@NotNull WeekShiftsEntity entity) {
//        weeks.put(entity.getDate(), entity);
//    }


    public List<ShiftEntity> getShiftsByBranchAndWeek(int branchId, int year, int week) {
        List<ShiftEntity> shifts = shiftsTable.get(createKey(branchId, year, week));
        return shifts != null ? List.copyOf(shifts) : Collections.emptyList();
    }

    public void addUpdateShift(BranchWeekKey branchWeekKey, ShiftEntity shift) {
        List<ShiftEntity> defaultValue = new ArrayList<>();
        defaultValue.add(shift);
        shiftsTable.put(
                branchWeekKey,
                shiftsTable.getOrDefault(branchWeekKey, defaultValue)
        );
    }

    private BranchWeekKey createKey(int branchId, int year, int week) {
        return new BranchWeekKey(branchId, year, week);
    }
}
