package presentation.control;

import context.SessionManager;
import domain.entities.Employee;
import domain.entities.Role;
import domain.entities.Shift;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import domain.services.EmployeeService;
import domain.services.HRManagerShiftService;
import domain.services.RoleService;
import domain.services.ShiftService;

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
            hrService.setJobsToShift(shift, jobs);
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
        //todo
        return null;
    }

    public void openShift(int day, int type, String id) {
        WeekDay weekDay = WeekDay.values()[day];
        ShiftType shiftType = ShiftType.values()[type];
        Employee employee = employeeService.getEmployeeDetails(id);
        if (employee == null) throw new IllegalArgumentException("Employee does not exist");
        if (employee.getQualifiedRoles().contains(Role.ShiftManager))
            service.updateShift(SessionManager.now().toLocalDate(), weekDay,
                    shiftType, new Shift(weekDay, shiftType, id));
        else throw new IllegalArgumentException("Employee is not a shift manager");
    }

    public void closeShift(int day, int type) {
        WeekDay weekDay = WeekDay.values()[day];
        ShiftType shiftType = ShiftType.values()[type];
        service.updateShift(SessionManager.now().toLocalDate(), weekDay,
                shiftType, null);
    }
}
