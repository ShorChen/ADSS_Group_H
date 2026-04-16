package presentation.control;

import domain.entities.*;
import domain.services.ShiftService;
import domain.util.TimeInterval;

import java.util.Date;
import java.util.List;
import java.util.Map;

/* requirement no. 6 7 9 18 24 25 32*/
public class HRManagerShiftController {
    private ShiftService service;

    public HRManagerShiftController(ShiftService service) {
        this.service = service;
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

    public void setDeadline(Date date) {
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
