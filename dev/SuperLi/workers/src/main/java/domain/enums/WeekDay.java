package domain.enums;

// ordinal() method is being used in code!
// DO NOT CHANGE THE ORDER OF ENTRIES!
public enum WeekDay {
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

}
