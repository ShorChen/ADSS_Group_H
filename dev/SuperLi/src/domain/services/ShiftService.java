package domain.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class ShiftService {
    public Map<Integer, Map<Integer, Character>> getNWeeksAgo(LocalDate date) {
        System.out.println("Implement Method");
        return null;
    }

    public void setDeadline(LocalDateTime dateTime) {
        System.out.println("Implement Method");
        if (dateTime.minusDays(1).isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Deadline date must be at least one day from now");
    }
}
