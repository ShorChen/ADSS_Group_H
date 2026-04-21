package domain.entities;

import data_access.entities.ShiftEntity;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import util.BoundedSet;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Shift {
    private final LocalDateTime startDate;
    private final WeekDay day;
    private final ShiftType shiftType;
    private final Map<Role, BoundedSet<String>> employees;
    private final Map<String, Float> additionalHours;

    public Shift(LocalDateTime startDate, WeekDay day, ShiftType shiftType,
                 Map<Role, BoundedSet<String>> employees,
                 Map<String, Float> additionalHours) {
        this.startDate = startDate;
        this.day = day;
        this.shiftType = shiftType;
        this.employees = employees;
        this.additionalHours = additionalHours;
    }

    public Shift(WeekDay day, ShiftType type, String id) {
        startDate = null;
        this.day = day;
        this.shiftType = type;
        employees = new HashMap<>();

        BoundedSet<String> shiftManager = new BoundedSet<>(1);
        shiftManager.add(id);

        employees.put(Role.ShiftManager, shiftManager);

        additionalHours = new HashMap<>();
    }

    public Shift(ShiftEntity entity) {
        this(
                entity.getStartDate(), WeekDay.valueOf(entity.getDay()),
                ShiftType.valueOf(entity.getShiftType()), new HashMap<>(),
                entity.getAdditionalHours()
        );

        entity.getEmployees().forEach((role, employeesSet) -> {
            BoundedSet<String> employeesStringSet = new BoundedSet<>(employeesSet.size());
            employeesStringSet.addAll(employeesSet);
            this.employees.put(new Role(role), employeesStringSet);
        });
    }

    public ShiftEntity toEntity() {
        Map<String, Set<String>> rolesToEmployees = new HashMap<>();
        employees.forEach((role, employeesSet) ->
                rolesToEmployees.put(role.getTag(), employeesSet));
        Map<String, Float> employeesAdditionalHours = new HashMap<>(additionalHours);

        return new ShiftEntity(startDate, day.name(), shiftType.name(),
                rolesToEmployees, employeesAdditionalHours
        );
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public WeekDay getDay() {
        return day;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public String getShiftManager() { //todo: test
        return employees.get(Role.ShiftManager).toArray(String[]::new)[0];
    }

    public Map<Role, BoundedSet<String>> getEmployees() {
        return employees;
    }

    public Map<String, Float> getAdditionalHours() {
        return additionalHours;
    }

    public boolean assign(Role role, Employee employee) {
        if (!employees.containsKey(role)) employees.put(role,
                new BoundedSet<>(5));
        if (employee.getQualifiedRoles().contains(role)) {
            return employees.get(role).add(employee.getId());
        }
        return false;
    }

    public boolean addJob(Role role, int capacity) {
        if (employees.containsKey(role)) {
            if (employees.get(role).getMaximumCapacity() == capacity) return false;
            employees.get(role).setMaximumCapacity(capacity);
        } else {
            employees.put(role, new BoundedSet<>(capacity));
        }
        return true;
    }

}
