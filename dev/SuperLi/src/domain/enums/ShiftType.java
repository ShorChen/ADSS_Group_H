package domain.enums;

public enum ShiftType {
    DAY("Day"),
    EVENING("Evening");
    public final String type;

    ShiftType(String type) {
        this.type = type;
    }
}
