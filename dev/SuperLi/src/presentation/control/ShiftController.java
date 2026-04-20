package presentation.control;

import domain.entities.WeekShifts;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import domain.services.ShiftService;

import java.time.LocalDate;
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

        WeekShifts week = service.getNWeeksAgo(LocalDate.now().minusWeeks(weeksAgo));

        Map<Integer, Map<Integer, Character>> map = new HashMap<>();
        for (int i = 0; i < WeekDay.values().length; i++)
            map.put(WeekDay.values()[i].day, of('X', 'X'));

        week.getShifts().forEach(shift ->
                map.get(shift.getDay().day).put(shift.getShiftType().ordinal(), ' '));

        return map;
    }

    private Map<Integer, Character> of(char day, char night) {
        Map<Integer, Character> map = new HashMap<>();
        map.put(0, day);
        map.put(1, night);
        return map;
    }
}
