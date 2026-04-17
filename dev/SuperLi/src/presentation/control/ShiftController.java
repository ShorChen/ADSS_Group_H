package presentation.control;

import domain.enums.ShiftType;
import domain.enums.WeekDay;
import domain.services.ShiftService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

public class ShiftController {
    private static final int INDEX_SUN_DAY = 116;
    private static final int INDEX_SUN_NIGHT = 188;
    private static final int MARGIN = 4;
    private final ShiftService service;

    public ShiftController() {
        service = new ShiftService();
    }

    public int getIndexOf(int dayId, int typeId) {
        WeekDay day = WeekDay.values()[dayId];
        ShiftType type = typeId == 0 ? ShiftType.DAY : ShiftType.EVENING;
        int i = type == ShiftType.DAY ? 0 : 1;
        return day.day * MARGIN + INDEX_SUN_DAY +
               (INDEX_SUN_NIGHT - INDEX_SUN_DAY) * i;
    }

    public Map<Integer, Map<Integer, Character>> getNWeeksAgo(int weeksAgo) {
        LocalDate date = LocalDate.now().minusWeeks(weeksAgo)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        // return service.getNWeeksAgo(date);
        return tempGetNWeeks();
    }

    private Map<Integer, Map<Integer, Character>> tempGetNWeeks() {
       Map<Integer, Map<Integer, Character>> map = new HashMap<>();
        map.put(0, of('5', '3'));
        map.put(1, of('2', '2'));
        map.put(2, of('6', '7'));
        map.put(3, of(' ', ' '));
        map.put(4, of(' ', ' '));
        map.put(5, of(' ', ' '));
        map.put(6, of('X', 'X'));

        return map;
    }

    private Map<Integer, Character> of(char day, char night){
        Map<Integer, Character> map = new HashMap<>();
        map.put(0, day);
        map.put(1, night);
        return map;
    }
}
