package Workers.Domain.DTO;

import Workers.Shared.Enums.ShiftType;
import Workers.Shared.Enums.WeekDay;

public record ShiftKey(WeekDay day, ShiftType shiftType) {
}
