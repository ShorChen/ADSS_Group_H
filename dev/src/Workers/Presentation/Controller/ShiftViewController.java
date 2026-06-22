package Workers.Presentation.Controller;

import Workers.Context.SessionManager;
import Workers.Domain.DTO.ShiftSL;
import Workers.Domain.DTO.ShiftKey;
import Workers.Service.EmployeeService;
import Workers.Service.ShiftService;
import Workers.Presentation.DTO.EmployeePL;
import Workers.Presentation.DTO.ShiftPL;
import Workers.Shared.Enums.ShiftType;
import Workers.Shared.Enums.WeekDay;
import Workers.Shared.WeekConstants;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShiftViewController {
    private final ShiftService shiftService;
    private final EmployeeService employeeService;

    public ShiftViewController() {
        shiftService = new ShiftService();
        employeeService = new EmployeeService();
    }

    public Map<ShiftKey, ShiftPL> getWeek(int weeksAgo) {
        LocalDate targetDate = SessionManager.now().minusWeeks(weeksAgo).toLocalDate();
        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getSelectedBranchId();

        Map<ShiftKey, ShiftSL> map = shiftService.getShiftsOfWeek(branchId, year, week);
        Map<ShiftKey, ShiftPL> weekShifts = new LinkedHashMap<>();

        for (int i = 0; i < WeekDay.values().length; i++) {
            for (int j = 0; j < ShiftType.values().length; j++) {
                weekShifts.put(new ShiftKey(WeekDay.fromInteger(i),
                        ShiftType.fromInteger(j)), null);
            }
        }

        map.forEach((key, value) ->
                weekShifts.put(key, new ShiftPL(value))
        );
        return weekShifts;
    }

    public EmployeePL getEmployeeDetails(String id) {
        return new EmployeePL(employeeService.getEmployeeDetails(id));
    }


}
