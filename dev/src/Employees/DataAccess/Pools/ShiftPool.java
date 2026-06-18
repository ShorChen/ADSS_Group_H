package Employees.DataAccess.Pools;

import Employees.DataAccess.Entities.ShiftEntity;
import Employees.DataAccess.Entities.Keys.BranchWeekKey;
import Employees.DataAccess.Entities.Keys.ShiftEntityKey;

import java.util.*;

public class ShiftPool {
    private final Map<BranchWeekKey, Map<ShiftEntityKey, ShiftEntity>> shiftsTable;
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
        List<ShiftEntity> shifts = new ArrayList<>();
        shiftsTable.getOrDefault(createKey(branchId, year, week), new HashMap<>())
                .forEach((_, shift) ->
                        shifts.add(shift)
                );
        return shifts;
    }

    public void addUpdateShift(BranchWeekKey branchWeekKey, ShiftEntityKey shiftEntityKey,
                               ShiftEntity shift) {
        Map<ShiftEntityKey, ShiftEntity> week =
                shiftsTable.computeIfAbsent(branchWeekKey, _ -> new HashMap<>());
        week.put(shiftEntityKey, shift);
    }

    public void removeShift(int branchId, int year, int week, String day, String type) {
        shiftsTable.getOrDefault(createKey(branchId, year, week), new HashMap<>())
                .remove(new ShiftEntityKey(day, type));
    }

    private BranchWeekKey createKey(int branchId, int year, int week) {
        return new BranchWeekKey(branchId, year, week);
    }

    public static void free() {
        instance = null;
    }


}
