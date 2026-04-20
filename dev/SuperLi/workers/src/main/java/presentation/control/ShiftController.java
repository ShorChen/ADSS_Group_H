package presentation.control;

import domain.entities.WeekShifts;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import domain.services.ShiftService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class ShiftController {
    private static final int INDEX_SUN_DAY = 116;
    private static final int INDEX_SUN_NIGHT = 188;
    private static final int MARGIN = 4;
    private final ShiftService service;

    public ShiftController() {
        service = new ShiftService();
    }


    public Map<Integer, Map<Integer, Character>> getNWeeksAgo(int weeksAgo) {

        WeekShifts week = service.getNWeeksAgo(LocalDate.now().minusWeeks(weeksAgo));

        Map<Integer, Map<Integer, Character>> map = new HashMap<>();
        for (int i = 0; i < WeekDay.values().length; i++)
            map.put(WeekDay.values()[i].ordinal(), of('X', 'X'));

        week.getShifts().forEach(shift ->
                map.get(shift.getDay().ordinal()).put(shift.getShiftType().ordinal(), ' '));

        return map;
    }

    private Map<Integer, Character> of(char day, char night) {
        Map<Integer, Character> map = new HashMap<>();
        map.put(0, day);
        map.put(1, night);
        return map;
    }
}
