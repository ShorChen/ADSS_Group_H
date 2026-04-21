package domain.services;

import data_access.entities.ShiftEntity;
import data_access.entities.WeekShiftsEntity;
import data_access.pools.ShiftPool;
import domain.entities.Shift;
import domain.entities.WeekShifts;
import domain.enums.ShiftType;
import domain.enums.WeekDay;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShiftService {
    private final ShiftPool shiftPool;

    public ShiftService() {
        shiftPool = ShiftPool.Instance();
    }

    public WeekShifts getNWeeksAgo(LocalDate date) {
        LocalDate weekDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        WeekShiftsEntity weekEntity = shiftPool.getWeek(weekDate);
        if (weekEntity == null)
            return new WeekShifts(weekDate, new HashMap<>(), new HashMap<>());
        return new WeekShifts(weekEntity);
    }

    public void updateWeek(WeekShifts weekShifts) {
        shiftPool.updateWeek(weekShifts.toEntity());
    }

    public void updateShift(LocalDate date, WeekDay day, ShiftType type, Shift shift) {
        LocalDate weekDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        WeekShiftsEntity week = shiftPool.getWeek(weekDate);
        ShiftEntity shiftEntity = shift == null ? null : shift.toEntity();
        if (type == ShiftType.DAY) week.addDayShift(day.name(), shiftEntity);
        else if (type == ShiftType.EVENING) week.addNightShift(day.name(), shiftEntity);
    }

    public void setClosedDays(List<String> closed) {
        shiftPool.setClosedDays(closed);
    }

    public List<WeekDay> getClosedDays() {
        List<WeekDay> closedDays = new ArrayList<>();
        shiftPool.getClosedDays().forEach(s
                -> closedDays.add(WeekDay.valueOf(s)));
        return closedDays;
    }
}
