package data_access.entities;

import java.time.LocalDateTime;
import java.util.Map;

public class ShiftEntity {
    private final LocalDateTime startDate;
    private final String day;
    private final String shiftType;
    private final String shiftManager;
    private final Map<String, String> employees;
    private final Map<String, Float> additionalHours;

    public ShiftEntity(LocalDateTime startDate, String day,
                       String shiftType, Map<String, String> employees,
                       Map<String, Float> additionalHours, String shiftManager) {
        this.startDate = startDate;
        this.day = day;
        this.shiftType = shiftType;
        this.employees = employees;
        this.additionalHours = additionalHours;
        this.shiftManager = shiftManager;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public String getDay() {
        return day;
    }

    public String getShiftType() {
        return shiftType;
    }

    public String getShiftManager() {
        return shiftManager;
    }

    public Map<String, String> getEmployees() {
        return employees;
    }

    public Map<String, Float> getAdditionalHours() {
        return additionalHours;
    }
}
