package Employees.Presentation.Controller;

import Employees.Context.SessionManager;
import Employees.Domain.DTO.ShiftKey;
import Employees.Domain.DTO.ShiftSL;
import Employees.Presentation.DTO.ShiftPL;
import Employees.Service.ShiftService;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;
import Employees.Shared.WeekConstants;

import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;

public class EmployeeAdditionalHoursController {
    private final ShiftService service;

    public EmployeeAdditionalHoursController() {
        service = new ShiftService();
    }

    public void updateAdditionalHours(WeekDay day, ShiftType type, float hours) {
        LocalDate targetDate = SessionManager.now().toLocalDate();

        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getSelectedBranchId();
        String id = SessionManager.getCurrentEmployee().getId();

        ShiftSL s = service.getShiftsOfWeek(branchId, year, week).get(
                new ShiftKey(day, type)
        );

        Map<String, Float> additionalHours = s.getAdditionalHours();
        additionalHours.put(id, hours);
        s.setAdditionalHours(additionalHours);

        service.addUpdateShift(branchId, year, week, day.toString(), type.toString(), s);
    }

    public Function<ShiftPL, Boolean> getPastDayShiftsOfEmployeeThisWeekPredicate() {
        return shift ->
                shift != null && shift.getShiftType() == ShiftType.DAY
                && shift.getStartDate().isBefore(SessionManager.now())
                && shift.isAssigned(SessionManager.getCurrentEmployee().getId());
    }
}
