package Employees.Domain.DTO;

import Employees.DataAccess.Entities.ShiftEntity;
import org.jetbrains.annotations.NotNull;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShiftSL {
    private LocalDateTime startDate;
    private WeekDay day;
    private ShiftType shiftType;
    private Map<RoleSL, Set<String>> employees;
    private Map<RoleSL, Integer> capacities;
    private Map<String, Float> additionalHours;

    public ShiftSL(LocalDateTime startDate, WeekDay day, ShiftType shiftType,
                   Map<RoleSL, Set<String>> employees, Map<RoleSL, Integer> capacities,
                   Map<String, Float> additionalHours) {
        this.startDate = startDate;
        this.day = day;
        this.shiftType = shiftType;
        setEmployees(employees);
        setAdditionalHours(additionalHours);
        setCapacities(capacities);
    }

    public ShiftSL(LocalDateTime startDate, WeekDay day, ShiftType shiftType,
                   Map<RoleSL, Set<String>> employees,
                   Map<String, Float> additionalHours) {

        this(startDate, day, shiftType, employees, new HashMap<>(), additionalHours);
        employees.forEach((role, employeeIds) ->
                capacities.put(role, employeeIds.size())
        );
    }


    public ShiftSL(WeekDay day, ShiftType type, String shiftManagerId) {
        this(null, day, type, new HashMap<>(), new HashMap<>());

        HashSet<String> shiftManager = new HashSet<>();
        shiftManager.add(shiftManagerId);

        employees.put(RoleSL.ShiftManager, shiftManager);
    }

    public ShiftSL(@NotNull ShiftSL o) {
        this(o.startDate, o.day, o.shiftType, o.employees, o.capacities, o.additionalHours);
    }

    public ShiftSL(ShiftEntity entity) {
        this(entity.startDate(), WeekDay.fromArgs(entity.day()),
                ShiftType.fromType(entity.shiftType()), new HashMap<>(),
                entity.additionalHours()
        );
        entity.employees().forEach((role, employeeIds) ->
                employees.put(new RoleSL(role), new HashSet<>(employeeIds)));
    }

    public ShiftEntity toEntity() {
        Map<String, Set<String>> rolesToEmployees = new HashMap<>();
        employees.forEach((role, employeesSet) ->
                rolesToEmployees.put(role.getTag(), new HashSet<>(employeesSet)));

        return new ShiftEntity(startDate, day.name(), shiftType.name(),
                rolesToEmployees, new HashMap<>(additionalHours)
        );
    }

    // can be moved to the controller that would use it
    // convert ro doesEmployeeWork(Role role, String employeeId)
    public boolean doesEmployeeWork(String employeeId) {
        AtomicBoolean works = new AtomicBoolean(false);
        employees.forEach((role, employeeIds) -> {
            if (Objects.equals(role, RoleSL.ShiftManager))
                works.set(works.get() || employeeIds.contains(employeeId));
        });
        return works.get();
    }

    // can be moved to the controller that would use it
    // convert to canEmployeeWork(Role, String)
    public boolean canEmployeeWork(RoleSL role, String employeeId) {
        return !doesEmployeeWork(employeeId) &&
               capacities.getOrDefault(role, 1) >=
               employees.getOrDefault(role, new HashSet<>()).size();

    }

    public RoleSL getEmployeeShiftRole(String id) {
        for (Map.Entry<RoleSL, Set<String>> entry : employees.entrySet()) {
            if (entry.getValue().contains(id)) return entry.getKey();
        }
        return null;
    }

    public void assignEmployeeToRole(RoleSL role, EmployeeSL employee) {
        if (!canEmployeeWork(role, employee.getId()))
            throw new IllegalArgumentException("Employee can not work in this shift");
        employees.get(role).add(employee.getId());
    }

    public boolean shiftRequiresRole(RoleSL role) {
        return !employees.getOrDefault(role, new HashSet<>()).isEmpty();
    }

    public void addRole(RoleSL role, int capacity) {
        if (employees.getOrDefault(role, new HashSet<>()).size() < capacity) {
            capacities.put(role, capacity);
        }
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public WeekDay getDay() {
        return day;
    }

    public void setDay(WeekDay day) {
        this.day = day;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public Map<RoleSL, Set<String>> getEmployees() {
        Map<RoleSL, Set<String>> result = new HashMap<>();
        employees.forEach((role, employeeIds) ->
                result.put(role, new HashSet<>(employeeIds)));
        return result;
    }

    public void setEmployees(Map<RoleSL, Set<String>> employees) {
        this.employees = new HashMap<>();
        employees.forEach((role, employeeIds) ->
                this.employees.put(role, new HashSet<>(employeeIds)));
    }

    public Map<String, Float> getAdditionalHours() {
        return new HashMap<>(additionalHours);
    }

    public void setAdditionalHours(Map<String, Float> additionalHours) {
        this.additionalHours = new HashMap<>();
        this.additionalHours.putAll(additionalHours);
    }

    public String getShiftManager() {
        return employees.get(RoleSL.ShiftManager).toArray(String[]::new)[0];
    }

    public int getCapacity(RoleSL role) {
        return capacities.getOrDefault(role, 0);
    }

    public void setCapacity(RoleSL role, int cap) {
        if (cap < 1) throw new IllegalArgumentException("Capacity must be at least 1");
        if (capacities.containsKey(role) &&
            cap < capacities.get(role))
            throw new IllegalArgumentException("Can not reduce capacity below the amount of employees registered to the shift");
        this.capacities.put(role, cap);
    }

    public void setCapacities(Map<RoleSL, Integer> capacities) {
        this.capacities = new HashMap<>();
        this.capacities.putAll(capacities);
    }

    public Map<RoleSL, Integer> getCapacities() {
        return new HashMap<>(capacities);
    }
}
