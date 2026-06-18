package Employees.Shared.Enums;

// ordinal() method is being used in code!
// DO NOT CHANGE THE ORDER OF ENTRIES!
public enum WeekDay{
    SUNDAY("Sunday"),
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday");

    public final String day;

    WeekDay(String day) {
        this.day = day;
    }

    // since the enum is 1 to 1, we can reconstruct an enum value from its arguments.
    public static WeekDay fromArgs(String day) {
        return valueOf(day.toUpperCase());
    }
    public static WeekDay fromInteger(int ordinal){
        return values()[ordinal];
    }

    public static int number(WeekDay day) {
        return day.ordinal();
    }

    @Override
    public String toString() {
        return day;
    }
}
