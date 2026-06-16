package data_access.entities.keys;

public record WeekKey(int year, int week) {
    public static final WeekKey NO_WEEK = new WeekKey(-1, -1);
}
