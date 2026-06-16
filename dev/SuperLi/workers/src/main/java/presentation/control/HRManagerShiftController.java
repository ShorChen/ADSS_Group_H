package presentation.control;

import context.SessionManager;
import domain.entities.Employee;
import domain.entities.Role;
import domain.entities.Shift;
import domain.services.EmployeeService;
import domain.services.HRManagerShiftService;
import domain.services.ShiftService;
import presentation.model.EmployeePL;
import shared.WeekConstants;
import shared.enums.ShiftType;
import shared.enums.WeekDay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class HRManagerShiftController {
    private final ShiftService service;
    private final HRManagerShiftService hrService;
    private final EmployeeService employeeService;

    public HRManagerShiftController() {
        this.service = new ShiftService();
        this.hrService = new HRManagerShiftService();
        this.employeeService = new EmployeeService();
    }

    public void setJobsToShift(Shift shift, Map<Role, Integer> jobs) {
        try {
            //hrService.setJobsToShift(shift, jobs);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error setting jobs to shift: " + e.getMessage());
        }
    }

    public void createShiftTemplate(String name, Map<Role, Integer> jobs) {
    }

    public void setShiftTemplateAsDefault(String templateName) {
    }

    public void placeToShifts(List<Employee> employees, List<Shift> shifts) {
        try {
            //hrService.placeToShifts(employees, shifts);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error during shift placement: " + e.getMessage());
        }
    }



    public void openShift(WeekDay day, ShiftType type, EmployeePL shiftManager) {
        LocalDate targetDate = SessionManager.now().plusWeeks(1).toLocalDate();
        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getCurrentEmployee().getBranchId();

        service.addUpdateShift(branchId, year, week, day.toString(), type.toString(),
                new Shift(day, type, shiftManager.getId()));
    }

    public void closeShift(WeekDay day, ShiftType type) {
        LocalDate targetDate = SessionManager.now().plusWeeks(1).toLocalDate();
        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getCurrentEmployee().getBranchId();

        service.closeShift(branchId, year, week, day.toString(), type.toString());
    }

    public EmployeePL getShiftManager(String id) {
        Employee employee = employeeService.getEmployeeDetails(id);
        if (employee == null) throw new IllegalArgumentException("Employee does not exist");
        if (!employee.getQualifiedRoles().contains(Role.ShiftManager))
            throw new IllegalArgumentException("Employee is not a shift manager");
        return new EmployeePL(employee);
    }
}
