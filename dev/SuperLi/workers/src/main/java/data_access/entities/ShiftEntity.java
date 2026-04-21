package data_access.entities;

import util.BoundedSet;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ShiftEntity {
    private final LocalDateTime startDate;
    private final String day;
    private final String shiftType;
    private Map<String, Set<String>> employees;
    private Map<String, Float> additionalHours;

    public ShiftEntity(LocalDateTime startDate, String day,
                       String shiftType, Map<String, Set<String>> employees,
                       Map<String, Float> additionalHours) {
        this.startDate = startDate;
        this.day = day;
        this.shiftType = shiftType;
        this.employees = employees;
        this.additionalHours = additionalHours;
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

    public Map<String, Set<String>> getEmployees() {
        Map<String, Set<String>> result = new HashMap<>();
        employees.forEach((key, value) -> {
            Set<String> set = new BoundedSet<>(value, value.size());
            result.put(key, set);
        });
        return result;
    }

    public Map<String, Float> getAdditionalHours() {
        return additionalHours;
    }


    public boolean update(ShiftEntity entity) {
        if (entity == null) return false;
        if (!Objects.equals(startDate, entity.startDate) ||
            !Objects.equals(day, entity.day) ||
            !Objects.equals(shiftType, entity.shiftType)) return false;

        employees = entity.getEmployees();
        additionalHours = entity.getAdditionalHours();
        return true;
    }
}
