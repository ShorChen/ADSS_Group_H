package Employees.Presentation.DTO;

import Employees.Domain.DTO.AvailabilitySubmissionSL;
import Employees.Domain.DTO.ShiftKey;

import java.util.HashMap;
import java.util.Map;

public class AvailabilitySubmissionPL {
    private Map<ShiftKey, Boolean> shifts; // <(day, type), can work>
    private boolean workingDoubles;

    public AvailabilitySubmissionPL() {
        this.shifts = new HashMap<>();
    }

    public AvailabilitySubmissionPL(Map<ShiftKey, Boolean> shifts) {
        setShifts(shifts);
    }

    public AvailabilitySubmissionSL toAvailabilitySubmission(String employeeId){
        return new AvailabilitySubmissionSL(
                employeeId,
                shifts,
                workingDoubles
        );
    }

    public Map<ShiftKey, Boolean> getShifts() {
        return new HashMap<>(shifts);
    }

    public void setShifts(Map<ShiftKey, Boolean> shifts) {
        this.shifts = new HashMap<>(shifts);
    }

    public boolean isWorkingDoubles() {
        return workingDoubles;
    }

    public boolean getShift(ShiftKey key){
        return shifts.getOrDefault(key, true);
    }

    public void setShift(ShiftKey key, boolean canWork){
        shifts.put(key, canWork);
    }

    public void setWorkingDoubles(boolean workingDoubles) {
        this.workingDoubles = workingDoubles;
    }
}
