package Workers.Domain.DTO;

import Workers.DataAccess.Entities.AvailabilitySubmissionEntity;
import Workers.DataAccess.Entities.Keys.ShiftEntityKey;
import Workers.Shared.Enums.ShiftType;
import Workers.Shared.Enums.WeekDay;

import java.util.HashMap;
import java.util.Map;

public class AvailabilitySubmissionSL {
    private String employeeId;
    private Map<ShiftKey, Boolean> shifts; // <(day, type), can work>
    private boolean workingDoubles;

    public AvailabilitySubmissionSL(String employeeId, Map<ShiftKey, Boolean> shifts, boolean workingDoubles) {
        this.employeeId = employeeId;
        this.shifts = new HashMap<>();
        this.shifts.putAll(shifts);
        this.workingDoubles = workingDoubles;
    }

    public AvailabilitySubmissionSL(AvailabilitySubmissionEntity e) {
        this(
                e.employeeId(),
                new HashMap<>(),
                e.workingDoubles()
        );

        Map<ShiftKey, Boolean> shifts = new HashMap<>();
        e.shifts().forEach((key, canWork) -> {
            shifts.put(new ShiftKey(WeekDay.fromArgs(key.day()),
                    ShiftType.fromType(key.type())), canWork);
        });
        this.shifts = shifts;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public Map<ShiftKey, Boolean> getShifts() {
        return new HashMap<>(shifts);
    }

    public boolean isWorkingDoubles() {
        return workingDoubles;
    }

    public AvailabilitySubmissionEntity toEntity() {
        Map<ShiftEntityKey, Boolean> shiftEntityKeyMap = new HashMap();
        shifts.forEach((key, canWork) ->
                shiftEntityKeyMap.put(new ShiftEntityKey(key.day().toString(), key.shiftType().toString()), canWork));
        return new AvailabilitySubmissionEntity(
                employeeId,
                shiftEntityKeyMap,
                workingDoubles
        );
    }

    public boolean getShift(ShiftKey key){
        return shifts.getOrDefault(key, true);
    }
}
