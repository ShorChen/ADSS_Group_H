package Employees.Domain.Entities;

import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;

public record ShiftKey(WeekDay day, ShiftType shiftType) {
}
