package presentation.control.shared;

import domain.entities.Shift;
import domain.entities.WeekShifts;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import domain.services.EmployeeService;
import domain.services.ShiftService;
import presentation.model.EmployeePL;
import presentation.model.ShiftPL;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ShiftViewController {
    private final ShiftService shiftService;
    private final EmployeeService employeeService;

    public ShiftViewController() {
        shiftService = new ShiftService();
        employeeService = new EmployeeService();
    }

    public Map<Integer, Map<Integer, ShiftPL>> getWeek(int weeksAgo) {
        WeekShifts week = shiftService.getNWeeksAgo(LocalDate.now().minusWeeks(weeksAgo));

        Map<Integer, Map<Integer, ShiftPL>> map = new HashMap<>();
        for (int i = 0; i < WeekDay.values().length; i++)
            map.put(i, of(week, WeekDay.values()[i]));

        return map;
    }

    private Map<Integer, ShiftPL> of(WeekShifts week, WeekDay day) {
        Map<Integer, ShiftPL> map = new HashMap<>();
        Shift shift = week.getDayShifts().getOrDefault(day, null);
        map.put(ShiftType.DAY.ordinal(), shift == null? null : new ShiftPL(shift));

        shift = week.getNightShifts().getOrDefault(day, null);
        map.put(ShiftType.EVENING.ordinal(), shift == null? null : new ShiftPL(shift));

        return map;
    }

    public EmployeePL getEmployeeDetails(String id) {
        return new EmployeePL(employeeService.getEmployeeDetails(id));
    }
}
