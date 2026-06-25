package Employees.Domain.Entities;

import java.util.HashMap;
import java.util.Map;

public class AvailabilitySubmissionDL {
    private String employeeId;
    private Map<String, Boolean> shifts;
    private boolean workingDoubles;

    public AvailabilitySubmissionDL(String employeeId, Map<String, Boolean> shifts, boolean workingDoubles) {
        this.employeeId = employeeId;
        this.shifts = new HashMap<>();
        if (shifts != null) this.shifts.putAll(shifts);
        this.workingDoubles = workingDoubles;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public Map<String, Boolean> getShifts() {
        return new HashMap<>(shifts);
    }

    public boolean isWorkingDoubles() {
        return workingDoubles;
    }

    public boolean getShift(String key) {
        return shifts.getOrDefault(key, true);
    }
}