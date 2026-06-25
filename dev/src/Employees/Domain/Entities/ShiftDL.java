package Employees.Domain.Entities;

import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShiftDL {
    public static final int DAY_SHIFT_START = 12;

    // NEW FIELDS absorbed from the deleted Keys
    private int shiftId;
    private int branchId;
    private int year;
    private int week;

    private LocalDateTime startDate;
    private WeekDay day;
    private ShiftType shiftType;
    private Map<RoleDL, Set<String>> employees;
    private Map<RoleDL, Integer> capacities;
    private Map<String, Float> additionalHours;

    // The Full Constructor used by the DAO
    public ShiftDL(int shiftId, int branchId, int year, int week, LocalDateTime startDate, WeekDay day, ShiftType shiftType,
                   Map<RoleDL, Set<String>> employees, Map<RoleDL, Integer> capacities, Map<String, Float> additionalHours) {
        this.shiftId = shiftId;
        this.branchId = branchId;
        this.year = year;
        this.week = week;
        this.startDate = startDate;
        this.day = day;
        this.shiftType = shiftType;
        setEmployees(employees);
        setAdditionalHours(additionalHours);
        if (capacities != null) {
            setCapacities(capacities);
        } else {
            this.capacities = new HashMap<>();
            employees.forEach((r, ids) -> this.capacities.put(r, ids.size()));
        }
    }

    // Constructor without capacities (auto-calculates them)
    public ShiftDL(int shiftId, int branchId, int year, int week, LocalDateTime startDate, WeekDay day, ShiftType shiftType,
                   Map<RoleDL, Set<String>> employees, Map<String, Float> additionalHours) {
        this(shiftId, branchId, year, week, startDate, day, shiftType, employees, null, additionalHours);
    }

    // Getters and Setters for the new fields
    public int getShiftId() { return shiftId; }
    public void setShiftId(int shiftId) { this.shiftId = shiftId; }
    public int getBranchId() { return branchId; }
    public int getYear() { return year; }
    public int getWeek() { return week; }

    public boolean doesEmployeeWork(String employeeId) {
        AtomicBoolean works = new AtomicBoolean(false);
        employees.forEach((role, employeeIds) -> {
            if (employeeIds.contains(employeeId)) works.set(true);
        });
        return works.get();
    }

    public boolean canEmployeeWork(RoleDL role, String employeeId) {
        Set<String> current = employees.getOrDefault(role, new HashSet<>());
        int capacity = capacities.getOrDefault(role, 0);
        return !current.contains(employeeId) && current.size() < capacity;
    }

    public RoleDL getEmployeeShiftRole(String id) {
        for (Map.Entry<RoleDL, Set<String>> entry : employees.entrySet()) {
            if (entry.getValue().contains(id)) return entry.getKey();
        }
        return null;
    }

    public void assignEmployeeToRole(RoleDL role, String employeeId) {
        Set<String> current = employees.computeIfAbsent(role, k -> new HashSet<>());
        if (doesEmployeeWork(employeeId)) {
            // Shift Manager is allowed to fill exactly ONE extra role per shift
            RoleDL currentRole = getEmployeeShiftRole(employeeId);
            boolean isShiftManager = currentRole != null &&
                    currentRole.getTag().equalsIgnoreCase("Shift Manager");
            if (!isShiftManager)
                throw new IllegalArgumentException("Employee is already assigned to this shift");
            // Count non-Shift-Manager assignments for this employee
            long extraRoles = employees.entrySet().stream()
                    .filter(e -> !e.getKey().getTag().equalsIgnoreCase("Shift Manager")
                            && e.getValue().contains(employeeId))
                    .count();
            if (extraRoles >= 1)
                throw new IllegalArgumentException("Shift Manager can only fill one extra role per shift");
        }
        if (current.size() >= capacities.getOrDefault(role, 0))
            throw new IllegalArgumentException("Capacity exceeded for role: " + role.getTag());
        current.add(employeeId);
    }

    public void replaceEmployees(RoleDL role, String prevEmployee, String newEmployee){
        if(!doesEmployeeWork(prevEmployee))
            throw new IllegalArgumentException("Employee does not work this shift");
        employees.get(role).remove(prevEmployee);
        assignEmployeeToRole(role, newEmployee);
    }

    public boolean shiftRequiresRole(RoleDL role) {
        return !employees.getOrDefault(role, new HashSet<>()).isEmpty();
    }

    public void addRole(RoleDL role, int capacity) {
        if (employees.getOrDefault(role, new HashSet<>()).size() < capacity) {
            capacities.put(role, capacity);
        }
    }

    public LocalDateTime getStartDate() { return startDate; }
    public WeekDay getDay() { return day; }
    public ShiftType getShiftType() { return shiftType; }

    public Map<RoleDL, Set<String>> getEmployees() {
        Map<RoleDL, Set<String>> result = new HashMap<>();
        employees.forEach((role, employeeIds) -> result.put(role, new HashSet<>(employeeIds)));
        return result;
    }

    public void setEmployees(Map<RoleDL, Set<String>> employees) {
        this.employees = new HashMap<>();
        if (employees != null) {
            employees.forEach((role, employeeIds) -> this.employees.put(role, new HashSet<>(employeeIds)));
        }
    }

    public Map<String, Float> getAdditionalHours() { return new HashMap<>(additionalHours); }
    public void setAdditionalHours(Map<String, Float> additionalHours) {
        this.additionalHours = new HashMap<>();
        if (additionalHours != null) this.additionalHours.putAll(additionalHours);
    }

    public int getCapacity(RoleDL role) { return capacities.getOrDefault(role, 0); }
    public void setCapacity(RoleDL role, int cap) {
        if (cap < 1) throw new IllegalArgumentException("Capacity must be at least 1");
        this.capacities.put(role, cap);
    }

    public void setCapacities(Map<RoleDL, Integer> capacities) {
        this.capacities = new HashMap<>();
        if (capacities != null) this.capacities.putAll(capacities);
    }
}