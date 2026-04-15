package domain.entities;

import java.util.List;

public class ExceptionalPlacementRequest {
    private long requestId;
    private List<Shift> shifts;
    private Employee employee;

    public long getRequestId() {
        return requestId;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public Employee getEmployee() {
        return employee;
    }
}
