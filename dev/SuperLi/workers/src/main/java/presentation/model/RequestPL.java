package presentation.model;

import domain.entities.Request;

public class RequestPL {

    private final ShiftPL shift;
    private final String prevEmployee;
    private final String newEmployee;
    private final String manager;
    private final boolean prevApproved;
    private final boolean newApproved;
    private final boolean managerApproved;
    private final boolean denied;

    public RequestPL(ShiftPL shift, String prevEmployee, String newEmployee,
                     String manager, boolean prevApproved, boolean newApproved,
                     boolean managerApproved, boolean denied) {
        this.shift = shift;
        this.prevEmployee = prevEmployee;
        this.newEmployee = newEmployee;
        this.manager = manager;
        this.prevApproved = prevApproved;
        this.newApproved = newApproved;
        this.managerApproved = managerApproved;
        this.denied = denied;
    }

    public RequestPL(ShiftPL shift, String prevEmployee, String newEmployee) {
        this(shift, prevEmployee, newEmployee, null,
                true, false, false, false);
    }

    public RequestPL(Request request) {
        this(new ShiftPL(request.getShift()),
                request.getPrevEmployee(),
                request.getNewEmployee(),
                request.getManager(),
                request.isPrevApproved(),
                request.isNewApproved(),
                request.isManagerApproved(),
                request.isDenied()
        );
    }

    public ShiftPL getShift() {
        return shift;
    }

    public Request toRequest() {
        return new Request(
                shift.toShift(), prevEmployee, newEmployee, manager,
                prevApproved, newApproved, managerApproved, denied
        );
    }

    public Request toReplacementRequest() {
        return new Request(shift.toShift(), prevEmployee, newEmployee, manager,
                prevApproved, newApproved, managerApproved, denied);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Day: ").append(shift.getDay().day)
                .append(" At: ").append(shift.getShiftType().type).append("\n");

        if (newEmployee != null) {

        } else {
            String status = managerApproved ? "Approved" : "Denied";
            String prevStatus = prevApproved ? "Accepted" : "Pending";

            s.append(manager).append(" (").append(status).append(") on request: ")
                    .append(prevEmployee).append(" (").append(prevStatus).append(") ");
        }

        return s.toString();
    }
}
