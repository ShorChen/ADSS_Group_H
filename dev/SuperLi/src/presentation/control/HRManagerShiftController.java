package presentation.control;

import domain.entities.*;
import domain.services.ShiftService;
import domain.util.TimeInterval;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/* requirement no. 6 7 9 18 24 25 32*/
public class HRManagerShiftController {
    private ShiftService service;

    public HRManagerShiftController() {
        this.service = new ShiftService();
    }

    public void setJobsToShift(Shift shift, Map<Role, Integer> jobs) {

    }

    public void createShiftTemplate(String name, Map<Role, Integer> jobs) {
    }

    public void setShiftTemplateAsDefault(String templateName) {
    }

    public void placeToShifts(List<Employee> employees, List<Shift> shifts) {
    }

    public void requestExceptionalShiftPlacement(ExceptionalPlacementRequest request) {
    }

    public void setDeadline(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
            service.setDeadline(dateTime);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void handleReplacementRequest(ReplacementRequest request, boolean approve) {
    }

    public void setWorkingHours(Map<String, List<TimeInterval>> hours) {
    }

    public Report issueReport() {
        //todo
        return null;
    }
}
