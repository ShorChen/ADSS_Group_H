package domain.entities;

import java.util.List;

public class ReplacementRequest {
    private long requestId;
    private List<Shift> shifts;
    private Employee prevEmployee;
    private Employee newEmployee;

    public long getRequestId() {
        return requestId;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public Employee getPrevEmployee() {
        return prevEmployee;
    }

    public Employee getNewEmployee() {
        return newEmployee;
    }
}
