package presentation.model;

import domain.entities.Request;

public class RequestPL {

    private ShiftPL shift;
    private String prevEmployee;
    private String newEmployee;
    private String manager;
    private boolean prevApproved;
    private boolean newApproved;
    private boolean managerApproved;
    private boolean denied;

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
                false, false, false, false);
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

    public String getPrevEmployee() {
        return prevEmployee;
    }

    public Request toRequest() {
        return new Request(
                shift.toShift(), prevEmployee, newEmployee, manager,
                prevApproved, newApproved, managerApproved, denied
        );
    }


    public boolean isPrevApproved() {
        return prevApproved;
    }


    public boolean isNewApproved() {
        return newApproved;
    }

    public boolean isManagerApproved() {
        return managerApproved;
    }

    public boolean isDenied() {
        return denied;
    }

    public Request toReplacementRequest() {
        return new Request(shift.toShift(), prevEmployee, newEmployee, manager,
                prevApproved, newApproved, managerApproved, denied);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Day: ").append(shift.getDay().day)
                .append("At: ").append(shift.getShiftType().type).append("\n");

        if (newEmployee != null) {
            if (manager != null) {
                String status = managerApproved ? "Approved" : "Denied";
                s.append(manager).append(" (").append(status).append(") on request: ");
            }
            String prevStatus = prevApproved ? "Accepted" : "Denied";
            String newStatus = newApproved ? "Accepted" : denied ? "Denied" : "Pending";

            s.append(newEmployee).append(" (").append(newStatus).append(") ")
                    .append("==>")
                    .append(prevEmployee).append(" (").append(prevStatus).append(") ");
        } else {
            String status = managerApproved ? "Approved" : "Denied";
            String prevStatus = prevApproved ? "Accepted" : "Pending";

            s.append(manager).append(" (").append(status).append(") on request: ")
                    .append(prevEmployee).append(" (").append(prevStatus).append(") ");
        }

        return s.toString();
    }
}
