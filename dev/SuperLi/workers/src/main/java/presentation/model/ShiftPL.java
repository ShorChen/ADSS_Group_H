package presentation.model;

import domain.entities.Role;
import domain.entities.Shift;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import util.BoundedSet;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ShiftPL {

    private final LocalDateTime startDate;
    private final WeekDay day;
    private final ShiftType shiftType;
    private final Map<String, BoundedSet<String>> employees;
    private final Map<String, Float> additionalHours;


    public ShiftPL(LocalDateTime startDate, WeekDay day, ShiftType shiftType, Map<String, BoundedSet<String>> employees, Map<String, Float> additionalHours) {
        this.startDate = startDate;
        this.day = day;
        this.shiftType = shiftType;
        this.employees = employees;
        this.additionalHours = additionalHours;
    }

    public Shift toShift() {
        Map<Role, BoundedSet<String>> shiftEmployees = new HashMap<>();
        employees.forEach((role, idSet) ->
                shiftEmployees.put(new Role(role), new BoundedSet<>(idSet, idSet.size())));
        return new Shift(
                startDate, day, shiftType,
                shiftEmployees, additionalHours
        );
    }

    public ShiftPL(Shift shift) {
        this(shift.getStartDate(), shift.getDay(), shift.getShiftType(),
                new HashMap<>(), shift.getAdditionalHours());

        shift.getEmployees().forEach((role, workers) ->
                employees.put(role.getTag(), new BoundedSet<>(workers, workers.size())));
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

    public Map<String, BoundedSet<String>> getEmployees() {
        return employees;
    }

    public Map<String, Float> getAdditionalHours() {
        return additionalHours;
    }
}
