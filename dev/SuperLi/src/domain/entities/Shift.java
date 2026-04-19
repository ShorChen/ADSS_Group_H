package domain.entities;

import domain.enums.ShiftType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Shift {
    private final long shiftId;
    private final Date startDate;
    private final ShiftType shiftType;
    private Employee shiftManager;
    private final Map<Employee, Role> employees;
    private final Map<Employee, Float> additionalHours;
    private Map<Role, Integer> requiredJobs = new HashMap<>();

    public Shift(Date startDate, ShiftType shiftType, Map<Employee, Role> employees,
                 Map<Employee, Float> additionalHours, Employee shiftManager,
                 long shiftId) {
        this.startDate = startDate;
        this.shiftType = shiftType;
        this.employees = employees;
        this.additionalHours = additionalHours;
        this.shiftManager = shiftManager;
        this.shiftId = shiftId;
    }

    public long getShiftId() {
        return shiftId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public Employee getShiftManager() {
        return shiftManager;
    }

    public Map<Employee, Role> getEmployees() {
        return employees;
    }

    public Map<Employee, Float> getAdditionalHours() {
        return additionalHours;
    }

    public void setShiftManager(Employee shiftManager) {
        this.shiftManager = shiftManager;
    }

    public Map<Role, Integer> getRequiredJobs() {
    return requiredJobs;
    }

    public void setRequiredJobs(Map<Role, Integer> requiredJobs) {
        this.requiredJobs = requiredJobs;
    }
}
