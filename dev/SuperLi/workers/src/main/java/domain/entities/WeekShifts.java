package domain.entities;

import data_access.entities.ShiftEntity;
import data_access.entities.WeekShiftsEntity;
import domain.enums.ShiftType;
import domain.enums.WeekDay;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeekShifts {
    private LocalDate date;
    private Map<WeekDay, Shift> dayShifts;
    private Map<WeekDay, Shift> nightShifts;

    public WeekShifts(LocalDate date, Map<WeekDay, Shift> dayShifts,
                      Map<WeekDay, Shift> nightShifts) {
        this.date = date;
        this.dayShifts = dayShifts;
        this.nightShifts = nightShifts;
    }

    public WeekShifts(WeekShiftsEntity shiftsEntity) {
        this(shiftsEntity.getDate(), new HashMap<>(), new HashMap<>());
        shiftsEntity.getDayShifts().forEach((day, entity) -> {
            Shift shift = entity == null? null : new Shift(entity);
            dayShifts.put(WeekDay.valueOf(day), shift);
        });
        shiftsEntity.getNightShifts().forEach((day, entity) -> {
            Shift shift = entity == null? null : new Shift(entity);
            nightShifts.put(WeekDay.valueOf(day), shift);
        });
    }

    public WeekShiftsEntity toEntity() {
        Map<String, ShiftEntity> dayShiftEntityMap = new HashMap<>();
        dayShifts.forEach((day, shift) ->
                dayShiftEntityMap.put(day.name(), shift.toEntity())
        );
        Map<String, ShiftEntity> nightShiftEntityMap = new HashMap<>();
        nightShifts.forEach((day, shift) ->
                nightShiftEntityMap.put(day.name(), shift.toEntity())
        );
        return new WeekShiftsEntity(date, dayShiftEntityMap, nightShiftEntityMap);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<WeekDay, Shift> getDayShifts() {
        return dayShifts;
    }

    public void setDayShifts(Map<WeekDay, Shift> dayShifts) {
        this.dayShifts = dayShifts;
    }

    public Map<WeekDay, Shift> getNightShifts() {
        return nightShifts;
    }

    public List<Shift> getShifts() {
        List<Shift> shifts = new ArrayList<>();
        dayShifts.forEach((day, shift) -> shifts.add(shift));
        nightShifts.forEach((day, shift) -> shifts.add(shift));
        return shifts;
    }

    public void setNightShifts(Map<WeekDay, Shift> nightShifts) {
        this.nightShifts = nightShifts;
    }

    public void addDayShift(WeekDay day, Shift shift) {
        dayShifts.put(day, shift);
    }

    public void addNightShift(WeekDay day, Shift shift) {
        nightShifts.put(day, shift);
    }

    public Shift getShift(WeekDay day, ShiftType shiftType) {
        if (dayShifts.containsKey(day) &&
            dayShifts.get(day) != null &&
            dayShifts.get(day).getShiftType().equals(shiftType))
            return dayShifts.get(day);
        if (nightShifts.containsKey(day) &&
            nightShifts.get(day) != null &&
            nightShifts.get(day).getShiftType().equals(shiftType))
            return nightShifts.get(day);
        return null;
    }
}
