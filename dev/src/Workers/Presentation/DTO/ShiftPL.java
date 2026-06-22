package Workers.Presentation.DTO;

import Workers.Domain.DTO.RoleSL;
import Workers.Domain.DTO.ShiftSL;
import Workers.Shared.Enums.ShiftType;
import Workers.Shared.Enums.WeekDay;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShiftPL {
    private LocalDateTime startDate;
    private WeekDay day;
    private ShiftType shiftType;
    private Map<String, Set<String>> employees;
    private Map<RoleSL, Integer> capacities;
    private Map<String, Float> additionalHours;


    public ShiftPL(LocalDateTime startDate, WeekDay day, ShiftType shiftType,
                   Map<String, Set<String>> employees, Map<RoleSL, Integer> capacities,
                   Map<String, Float> additionalHours) {
        this.startDate = startDate;
        this.day = day;
        this.shiftType = shiftType;
        setEmployees(employees);
        setCapacities(capacities);
        setAdditionalHours(additionalHours);

    }

    public ShiftSL toShift() {
        Map<RoleSL, Set<String>> shiftEmployees = new HashMap<>();
        employees.forEach((role, idSet) ->
                shiftEmployees.put(new RoleSL(role),
                        new HashSet<>(idSet)
                )
        );
        return new ShiftSL(
                startDate, day, shiftType,
                shiftEmployees, capacities, additionalHours
        );
    }


    public ShiftPL(ShiftSL shift) {
        this(shift.getStartDate(), shift.getDay(), shift.getShiftType(),
                new HashMap<>(), shift.getCapacities(), shift.getAdditionalHours());

        shift.getEmployees().forEach((role, employeeIds) ->
                employees.put(role.getTag(), new HashSet<>(employeeIds)));
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

    /**
     * @return a map of role to set of employee ids working in this shift
     * @apiNote the method returns a deep copy of the map.
     */
    public Map<String, Set<String>> getEmployees() {
        Map<String, Set<String>> map = new HashMap<>();
        employees.forEach((role, ids) -> {
            map.put(role, new HashSet<>(ids));
        });
        return map;
    }

    public void setEmployees(Map<String, Set<String>> employees) {
        this.employees = new HashMap<>();
        employees.forEach((key, value) ->
                this.employees.put(key, new HashSet<>(value))
        );
    }

    public Map<RoleSL, Integer> getCapacities() {
        return new HashMap<>(capacities);
    }

    public void setCapacities(Map<RoleSL, Integer> capacities) {
        this.capacities = new HashMap<>();
        this.capacities.putAll(capacities);
    }

    public void setAdditionalHours(Map<String, Float> additionalHours) {
        this.additionalHours = new HashMap<>();
        this.additionalHours.putAll(additionalHours);
    }

    /**
     * The method retrieves a map of worker id to its additional hours
     *
     * @return the additional hours of each employee on the shift
     * @apiNote the method returns a deep copy.
     */
    public Map<String, Float> getAdditionalHours() {
        return new HashMap<>(additionalHours);
    }

}
