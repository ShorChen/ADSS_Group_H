package domain.entities;

import shared.enums.ShiftType;
import shared.enums.WeekDay;

public record ShiftKey(WeekDay day, ShiftType shiftType) {
}
