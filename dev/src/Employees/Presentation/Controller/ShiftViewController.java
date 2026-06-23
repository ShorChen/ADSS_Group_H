package Employees.Presentation.Controller;

import Employees.Context.SessionManager;
import Employees.Domain.DTO.ShiftSL;
import Employees.Domain.DTO.ShiftKey;
import Employees.Service.EmployeeService;
import Employees.Service.ShiftService;
import Employees.Presentation.DTO.EmployeePL;
import Employees.Presentation.DTO.ShiftPL;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;
import Employees.Shared.WeekConstants;

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
