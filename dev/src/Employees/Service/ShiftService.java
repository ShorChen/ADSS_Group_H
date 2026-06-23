package Employees.Service;

import Employees.DataAccess.ShiftDAO;
import Employees.DataAccess.Entities.Keys.BranchWeekKey;
import Employees.DataAccess.Entities.Keys.ShiftEntityKey;
import Employees.DataAccess.Pools.ShiftPool;
import Employees.Domain.DTO.ShiftSL;
import Employees.Domain.DTO.ShiftKey;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ShiftService {
    private final ShiftDAO shiftPool;

    public ShiftService() {
        shiftPool = ShiftPool.Instance();
    }

//    public WeekShifts getNWeeksAgo(int weeksAgo) {
//        LocalDate weekDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
//        WeekShiftsEntity weekEntity = shiftPool.getWeek(weekDate);
//        if (weekEntity == null)
//            return new WeekShifts(weekDate, new HashMap<>());
//        return new WeekShifts(weekEntity);
//    }

//    @Deprecated
//    public void updateWeek(WeekShifts weekShifts) {
//        shiftPool.addUpdateWeek(weekShifts.toEntity());
//    }

//    @Deprecated
//    public void updateShift(LocalDate date, WeekDay day, ShiftType type, Shift shift) {
//        LocalDate weekDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
//        WeekShiftsEntity week = shiftPool.getWeek(weekDate);
//        ShiftEntity shiftEntity = shift == null ? ShiftEntity.EMPTY_SHIFT : shift.toEntity();
//        week.addUpdateShift(shiftEntity);
//    }

    public Map<ShiftKey, ShiftSL> getShiftsOfWeek(int branchId, int year, int week) {

        Map<ShiftKey, ShiftSL> weekMap = new HashMap<>();
        shiftPool.getShiftsByBranchAndWeek(branchId, year, week).forEach(shiftEntity -> {
            ShiftSL shift = new ShiftSL(shiftEntity);
            weekMap.put(new ShiftKey(shift.getDay(), shift.getShiftType()), shift);
        });
        return weekMap;
    }

    public void addUpdateShift(int branchId, int year, int week,
                               String day, String type,
                               @NotNull ShiftSL shift) {
        shiftPool.addUpdateShift(new BranchWeekKey(branchId, year, week),
                new ShiftEntityKey(day, type), shift.toEntity());
    }

    public void closeShift(int branchId, int year, int week,
                           String day, String type) {
        shiftPool.removeShift(branchId, year, week, day, type);
    }
}
