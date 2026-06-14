package Workers.shared;

import java.time.temporal.WeekFields;
import java.util.Locale;

public interface WeekConstants {
    // determines what is the first day of the week
    WeekFields WEEK_FIELDS = WeekFields.of(Locale.US); // starts at Sunday
}
