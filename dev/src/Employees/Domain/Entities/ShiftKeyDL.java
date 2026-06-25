package Employees.Domain.Entities;

import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;
import java.util.Objects;

public class ShiftKeyDL {
    private final WeekDay day;
    private final ShiftType type;

    public ShiftKeyDL(WeekDay day, ShiftType type) {
        this.day = day;
        this.type = type;
    }

    public WeekDay day() { return day; }
    public ShiftType type() { return type; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShiftKeyDL that = (ShiftKeyDL) o;
        return day == that.day && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, type);
    }
}