package Employees.Presentation.Controller;

import Employees.Context.SessionManager;
import Employees.Domain.Entities.Shift;
import Employees.Domain.Entities.ShiftKey;
import Employees.Shared.WeekConstants;
import Employees.Service.EmployeeService;
import Employees.Service.ShiftService;
import Employees.Presentation.Model.EmployeePL;
import Employees.Presentation.Model.ShiftPL;

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

    public Map<ShiftKey, ShiftPL> getWeek(int weeksAgo) {
        LocalDate targetDate = SessionManager.now().minusWeeks(weeksAgo).toLocalDate();
        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getCurrentEmployee().getBranchId();

        Map<ShiftKey, Shift> map = shiftService.getShiftsOfWeek(branchId, year, week);
        Map<ShiftKey, ShiftPL> weekShifts = new HashMap<>();
        map.forEach((key, value) ->
                weekShifts.put(key, new ShiftPL(value))
        );
        return weekShifts;
    }

    public EmployeePL getEmployeeDetails(String id) {
        return new EmployeePL(employeeService.getEmployeeDetails(id));
    }


}
