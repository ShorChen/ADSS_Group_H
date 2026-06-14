package Workers.presentation.control;

import Workers.context.SessionManager;
import Workers.domain.entities.Shift;
import Workers.domain.entities.ShiftKey;
import Workers.shared.WeekConstants;
import Workers.domain.services.EmployeeService;
import Workers.domain.services.ShiftService;
import Workers.presentation.model.EmployeePL;
import Workers.presentation.model.ShiftPL;

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
