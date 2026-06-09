package domain.entities;

import data_access.entities.ShiftEntity;
import org.jetbrains.annotations.NotNull;
import shared.enums.ShiftType;
import shared.enums.WeekDay;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Shift {
    public static final Shift EMPTY_SHIFT = new Shift(null, null,
            null, new HashMap<>(), new HashMap<>());
    private LocalDateTime startDate;
    private WeekDay day;
    private ShiftType shiftType;
    private Map<Role, Set<String>> employees;
    private Map<Role, Integer> capacities;
    private Map<String, Float> additionalHours;

    public Shift(LocalDateTime startDate, WeekDay day, ShiftType shiftType,
                 Map<Role, Set<String>> employees, Map<Role, Integer> capacities,
                 Map<String, Float> additionalHours) {
        this.startDate = startDate;
        this.day = day;
        this.shiftType = shiftType;
        setEmployees(employees);
        setAdditionalHours(additionalHours);
        setCapacities(capacities);
    }

    public Shift(LocalDateTime startDate, WeekDay day, ShiftType shiftType,
                 Map<Role, Set<String>> employees,
                 Map<String, Float> additionalHours) {

        this(startDate, day, shiftType, employees, new HashMap<>(), additionalHours);
        employees.forEach((role, employeeIds) ->
                capacities.put(role, employeeIds.size())
        );
    }


    public Shift(WeekDay day, ShiftType type, String shiftManagerId) {
        this(null, day, type, new HashMap<>(), new HashMap<>());

        HashSet<String> shiftManager = new HashSet<>();
        shiftManager.add(shiftManagerId);

        employees.put(Role.ShiftManager, shiftManager);
    }

    public Shift(@NotNull Shift o) {
        this(o.startDate, o.day, o.shiftType, o.employees, o.capacities, o.additionalHours);
    }

    public Shift(ShiftEntity entity) {
        this(entity.startDate(), WeekDay.fromArgs(entity.day()),
                ShiftType.fromType(entity.shiftType()), new HashMap<>(),
                entity.additionalHours()
        );

        entity.employees().forEach((role, employeeIds) ->
                this.employees.put(new Role(role), new HashSet<>(employeeIds)));
    }

    public ShiftEntity toEntity() {
        if (equals(EMPTY_SHIFT)) return ShiftEntity.EMPTY_SHIFT;

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
        employees.forEach((_, employeeIds) ->
                works.set(works.get() || employeeIds.contains(employeeId)));
        return works.get();
    }

    // can be moved to the controller that would use it
    // convert to canEmployeeWork(Role, String)
    public boolean canEmployeeWork(Role role, String employeeId) {
        return !doesEmployeeWork(employeeId) &&
               capacities.getOrDefault(role, 1) >=
               employees.getOrDefault(role, new HashSet<>()).size();

    }

    public Role getEmployeeShiftRole(String id) {
        for (Map.Entry<Role, Set<String>> entry : employees.entrySet()) {
            if (entry.getValue().contains(id)) return entry.getKey();
        }
        return null;
    }

    public void assignEmployeeToRole(Role role, Employee employee) {
        if (!canEmployeeWork(role, employee.getId()))
            throw new IllegalArgumentException("Employee can not work in this shift");
        employees.get(role).add(employee.getId());
    }

    public boolean shiftRequiresRole(Role role) {
        return !employees.getOrDefault(role, new HashSet<>()).isEmpty();
    }

    public void addRole(Role role, int capacity) {
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

    public Map<Role, Set<String>> getEmployees() {
        Map<Role, Set<String>> result = new HashMap<>();
        employees.forEach((role, employeeIds) ->
                result.put(role, new HashSet<>(employeeIds)));
        return result;
    }

    public void setEmployees(Map<Role, Set<String>> employees) {
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
        return employees.get(Role.ShiftManager).toArray(String[]::new)[0];
    }

    public int getCapacity(Role role) {
        return capacities.getOrDefault(role, 0);
    }

    public void setCapacity(Role role, int cap) {
        if (cap < 1) throw new IllegalArgumentException("Capacity must be at least 1");
        if (capacities.containsKey(role) &&
            cap < capacities.get(role))
            throw new IllegalArgumentException("Can not reduce capacity below the amount of employees registered to the shift");
        this.capacities.put(role, cap);
    }

    public void setCapacities(Map<Role, Integer> capacities) {
        this.capacities = new HashMap<>();
        this.capacities.putAll(capacities);
    }

    public Map<Role, Integer> getCapacities() {
        return new HashMap<>(capacities);
    }
}
