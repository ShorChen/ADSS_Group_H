package domain.services;

import domain.entities.*;
import domain.util.TimeInterval;
import data_access.pools.ShiftPool;
import data_access.pools.EmployeePool;

import java.util.Date;
import java.util.List;
import java.util.Map;

/* requirement no. 6 7 9 18 24 25 32*/
public class HRManagerShiftService {
    private final ShiftPool shiftPool;
    private final EmployeePool employeePool;

    public HRManagerShiftService(ShiftPool shiftPool, EmployeePool employeePool) {
        this.shiftPool = shiftPool;
        this.employeePool = employeePool;
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
