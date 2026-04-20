package domain.services;

import data_access.entities.WeekShiftsEntity;
import data_access.pools.ShiftPool;
import domain.entities.WeekShifts;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;

public class ShiftService {
    private ShiftPool shiftPool;

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

    public void setDeadline(LocalDateTime dateTime) {
        System.out.println("Implement Method");
        if (dateTime.minusDays(1).isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Deadline date must be at least one day from now");
    }
}
