package presentation.model;

import domain.entities.Role;
import domain.entities.Shift;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import util.BoundedSet;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShiftPL {


    private final LocalDateTime startDate;
    private final WeekDay day;
    private final ShiftType shiftType;
    private final Map<String, Set<String>> employees;
    private final Map<String, Float> additionalHours;


    public ShiftPL(LocalDateTime startDate, WeekDay day, ShiftType shiftType,
                   Map<String, Set<String>> employees, Map<String, Float> additionalHours) {
        this.startDate = startDate;
        this.day = day;
        this.shiftType = shiftType;
        this.employees = employees;
        this.additionalHours = additionalHours;
    }

    public Shift toShift() {
        Map<Role, BoundedSet<String>> shiftEmployees = new HashMap<>();
        employees.forEach((role, idSet) ->
                shiftEmployees.put(new Role(role),
                        new BoundedSet<>(idSet, idSet.size())
                )
        );
        return new Shift(
                startDate, day, shiftType,
                shiftEmployees, additionalHours
        );
    }


    public ShiftPL(Shift shift) {
        this(shift.getStartDate(), shift.getDay(), shift.getShiftType(),
                new HashMap<>(), shift.getAdditionalHours());

        shift.getEmployees().forEach((role, workers) ->
                employees.put(role.getTag(), new HashSet<>(workers)));
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

    /**
     *
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

    /**
     * The method retrieves a map of worker id to its additional hours
     * @return the additional hours of each employee on the shift
     * @apiNote the method returns a deep copy.
     */
    public Map<String, Float> getAdditionalHours() {
        return new HashMap<>(additionalHours);
    }

    public String find(String id) {
        for (Map.Entry<String, Set<String>> e : employees.entrySet()) {
            if (e.getValue().contains(id)) return e.getKey();
        }
        return null;
    }
}
