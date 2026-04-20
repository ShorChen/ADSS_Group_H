package domain.entities;

import data_access.entities.ShiftEntity;
import domain.enums.ShiftType;
import domain.enums.WeekDay;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Shift {
    private final LocalDateTime startDate;
    private final WeekDay day;
    private final ShiftType shiftType;
    private String shiftManager;
    private final Map<String, Role> employees;
    private final Map<String, Float> additionalHours;

    public Shift(LocalDateTime startDate, WeekDay day, ShiftType shiftType, Map<String, Role> employees,
                 Map<String, Float> additionalHours, String shiftManager) {
        this.startDate = startDate;
        this.day = day;
        this.shiftType = shiftType;
        this.employees = employees;
        this.additionalHours = additionalHours;
        this.shiftManager = shiftManager;
    }

    public Shift(ShiftEntity entity) {
        this(
                entity.getStartDate(), WeekDay.valueOf(entity.getDay()),
                ShiftType.valueOf(entity.getShiftType()), new HashMap<>(),
                entity.getAdditionalHours(), entity.getShiftManager()

        );

        entity.getEmployees().forEach((id, role) ->
                employees.put(id, new Role(role)));
    }

    public ShiftEntity toEntity() {
        Map<String, String> employeesToRoles = new HashMap<>();
        employees.forEach((employee, role) ->
                employeesToRoles.put(employee, role.getTag()));
        Map<String, Float> employeesAdditionalHours = new HashMap<>(additionalHours);

        return new ShiftEntity(startDate, day.name(), shiftType.name(),
                employeesToRoles, employeesAdditionalHours,
                shiftManager
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

    public String getShiftManager() {
        return shiftManager;
    }

    public Map<String, Role> getEmployees() {
        return employees;
    }

    public Map<String, Float> getAdditionalHours() {
        return additionalHours;
    }
}
