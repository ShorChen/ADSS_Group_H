package domain.entities;

import domain.enums.WeekDay;

import java.util.HashMap;
import java.util.Map;

public class WeekShifts {
    private Map<WeekDay, Character> dayShifts;
    private Map<WeekDay, Character> nightShifts;

    public WeekShifts() {
        this.dayShifts = new HashMap<>();
        this.nightShifts = new HashMap<>();
    }

    public Map<WeekDay, Character> getDayShifts() {
        return dayShifts;
    }

    public void setDayShifts(Map<WeekDay, Character> dayShifts) {
        this.dayShifts = dayShifts;
    }

    public Map<WeekDay, Character> getNightShifts() {
        return nightShifts;
    }

    public void setNightShifts(Map<WeekDay, Character> nightShifts) {
        this.nightShifts = nightShifts;
    }
}
