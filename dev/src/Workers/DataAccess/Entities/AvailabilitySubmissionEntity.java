package Workers.DataAccess.Entities;

import Workers.DataAccess.Entities.Keys.ShiftEntityKey;

import java.util.HashMap;
import java.util.Map;

public record AvailabilitySubmissionEntity(
        String employeeId,
        Map<ShiftEntityKey, Boolean> shifts,
        boolean workingDoubles
) {
    @Override
    public Map<ShiftEntityKey, Boolean> shifts() {
        return new HashMap<>(shifts);
    }
}
