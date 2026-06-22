package Workers.DataAccess.Entities;

import Workers.Shared.WeekConstants;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

public record ShiftEntity(
        int shiftId, LocalDateTime startDate, String day, String shiftType,
        Map<String, Set<String>> employees, Map<String, Float> additionalHours
) {
    private static final WeekFields WEEK_FIELDS = WeekConstants.WEEK_FIELDS;
    public static final int NO_ID = -1;

    public ShiftEntity(LocalDateTime startDate, String day,
                       String shiftType, Map<String, Set<String>> employees,
                       Map<String, Float> additionalHours) {
        this(NO_ID, startDate, day, shiftType, employees, additionalHours);
    }

    public ShiftEntity(int shiftId, LocalDateTime startDate, String day,
                       String shiftType, Map<String, Set<String>> employees,
                       Map<String, Float> additionalHours) {
        this.shiftId = shiftId;
        this.startDate = startDate;
        this.day = day;
        this.shiftType = shiftType;

        this.employees = new HashMap<>();
        employees.forEach((key, value) ->
                this.employees.put(key, new HashSet<>(value))
        );

        this.additionalHours = new HashMap<>(additionalHours);

    }

    @Override
    public @NotNull Map<String, Set<String>> employees() {
        Map<String, Set<String>> result = new HashMap<>();
        employees.forEach((key, value) ->
            result.put(key, new HashSet<>(value))
        );
        return result;
    }

    public @NotNull Map<String, Float> additionalHours() {
        return new HashMap<>(additionalHours);
    }

    public int getYear() {

        return startDate.get(WEEK_FIELDS.weekBasedYear());
    }

    public int getWeek() {
        return startDate.get(WEEK_FIELDS.weekOfWeekBasedYear());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ShiftEntity that)) return false;
        return shiftId == that.shiftId && Objects.equals(day, that.day) && Objects.equals(shiftType, that.shiftType) && Objects.equals(startDate, that.startDate) && Objects.equals(employees, that.employees) && Objects.equals(additionalHours, that.additionalHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shiftId, startDate, day, shiftType, employees, additionalHours);
    }
}
