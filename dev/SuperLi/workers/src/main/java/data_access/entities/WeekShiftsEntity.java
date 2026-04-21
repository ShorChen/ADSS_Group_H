package data_access.entities;

import java.time.LocalDate;
import java.util.Map;

public class WeekShiftsEntity {
    private LocalDate date;
    private Map<String, ShiftEntity> dayShifts;
    private Map<String, ShiftEntity> nightShifts;

    public WeekShiftsEntity(LocalDate date, Map<String, ShiftEntity> dayShifts,
                            Map<String, ShiftEntity> nightShifts) {
        this.date = date;
        this.dayShifts = dayShifts;
        this.nightShifts = nightShifts;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<String, ShiftEntity> getDayShifts() {
        return dayShifts;
    }

    public void setDayShifts(Map<String, ShiftEntity> dayShifts) {
        this.dayShifts = dayShifts;
    }

    public Map<String, ShiftEntity> getNightShifts() {
        return nightShifts;
    }

    public void setNightShifts(Map<String, ShiftEntity> nightShifts) {
        this.nightShifts = nightShifts;
    }

    public void addDayShift(String day, ShiftEntity shift) {
        dayShifts.put(day, shift);
    }

    public void addNightShift(String day, ShiftEntity shift) {
        nightShifts.put(shift.getDay(), shift);
    }

    public ShiftEntity getShift(String day, String shiftType) {
        if (dayShifts.containsKey(day) &&
            dayShifts.get(day).getShiftType().equals(shiftType))
            return dayShifts.get(day);
        if (nightShifts.containsKey(day) &&
            nightShifts.get(day).getShiftType().equals(shiftType))
            return nightShifts.get(day);
        return null;
    }

}
