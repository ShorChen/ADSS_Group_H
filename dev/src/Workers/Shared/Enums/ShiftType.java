package Workers.Shared.Enums;

import java.time.LocalTime;

public enum ShiftType {
    DAY("Day", LocalTime.of(12, 0), LocalTime.of(14, 0)),
    EVENING("Evening", LocalTime.of(14, 0), LocalTime.of(22,0));
    public final String type;
    public final LocalTime startTime;
    public final LocalTime endTime;

    ShiftType(String type, LocalTime startTime, LocalTime endTime) {
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static ShiftType fromType(String shiftType) {
        return valueOf(shiftType.toUpperCase());
    }

    public static ShiftType fromInteger(int ordinal) {
        return values()[ordinal];
    }

    public static int number(ShiftType shiftType) {
        return shiftType.ordinal();
    }

    @Override
    public String toString() {
        return type;
    }
}
