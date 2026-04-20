package presentation.control;

import domain.enums.ShiftType;
import domain.enums.WeekDay;
import presentation.model.RequestPL;

import java.util.*;

public class RequestReplacementController {

    public List<ShiftType> getOpenShifts(WeekDay i) {
        return Arrays.asList(ShiftType.values());
    }

    public boolean requestShiftReplacement(WeekDay[] day, ShiftType[] type, String id,
                                           String otherId) {

        return false;
    }

    public List<RequestPL> getPendingRequests(String id) {
        return new ArrayList<>();
    }

    public boolean approve(RequestPL request, String id) {
        return false;
    }

    public void deny(RequestPL request, String id) {
    }

    public Map<WeekDay, List<ShiftType>> getWorkingShifts(String id) {
        return getworkingshiftsdemo();
    }

    private Map<WeekDay, List<ShiftType>> getworkingshiftsdemo() {
        Map<WeekDay, List<ShiftType>> map = new LinkedHashMap<>();
        map.put(WeekDay.SUNDAY, of(true, true));
        map.put(WeekDay.MONDAY, of(true, true));
        map.put(WeekDay.TUESDAY, of(true, true));
        map.put(WeekDay.WEDNESDAY, of(false, true));
        map.put(WeekDay.THURSDAY, of(false, true));
        map.put(WeekDay.FRIDAY, of(true, true));
        map.put(WeekDay.SATURDAY, of(false, false));
        return map;
    }

    private List<ShiftType> of(boolean day, boolean night) {
        List<ShiftType> list = new ArrayList<>();
        if (day) list.add(ShiftType.DAY);
        if (night) list.add(ShiftType.EVENING);
        return list;
    }
}
