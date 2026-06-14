package Workers.domain.entities;

import Workers.shared.enums.ShiftType;
import Workers.shared.enums.WeekDay;

public record ShiftKey(WeekDay day, ShiftType shiftType) {
}
