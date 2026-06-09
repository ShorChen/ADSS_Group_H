package presentation.control;

import context.SessionManager;
import domain.entities.Employee;
import domain.entities.Role;
import domain.entities.Shift;
import shared.enums.ShiftType;
import shared.enums.WeekDay;
import domain.services.EmployeeService;
import domain.services.HRManagerShiftService;
import domain.services.ShiftService;
import presentation.model.EmployeePL;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class HRManagerShiftController {
    private ShiftService service;
    private HRManagerShiftService hrService;
    private EmployeeService employeeService;

    public HRManagerShiftController() {
        this.service = new ShiftService();
        this.hrService = new HRManagerShiftService();
        this.employeeService = new EmployeeService();
    }

    public void setJobsToShift(Shift shift, Map<Role, Integer> jobs) {
        try {
            //hrService.setJobsToShift(shift, jobs);
        } catch (IllegalArgumentException e) {
            System.out.println("Error setting jobs to shift: " + e.getMessage());
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
            System.out.println("Error during shift placement: " + e.getMessage());
        }
    }

    public void setDeadline(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
            if (!dateTime.minusDays(1).plusMinutes(1).isAfter(SessionManager.now()))
                throw new IllegalArgumentException("Deadline date must be at least one day from now");
            SessionManager.setDeadline(dateTime);

        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String issueReport() {
        // todo
        return null;
    }

    public void openShift(WeekDay day, ShiftType type, EmployeePL shiftManager) {
        service.addUpdateShift(SessionManager.now().toLocalDate(), day,
                type, new Shift(day, type, shiftManager.getId()));
    }

    public void closeShift(WeekDay day, ShiftType type) {
        service.addUpdateShift(SessionManager.now().toLocalDate(), day,
                type, null);
    }

    public EmployeePL getShiftManager(String id) {
        Employee employee = employeeService.getEmployeeDetails(id);
        if (employee == null) throw new IllegalArgumentException("Employee does not exist");
        if (!employee.getQualifiedRoles().contains(Role.ShiftManager))
            throw new IllegalArgumentException("Employee is not a shift manager");
        return new EmployeePL(employee);
    }
}
