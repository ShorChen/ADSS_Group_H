package Employees.Presentation.DTO;

import Employees.Domain.Entities.RequestSL;
import Employees.Shared.Enums.RequestStatus;

public class RequestPL {

    private ShiftPL shift;
    private String prevEmployee;
    private String newEmployee;
    private String manager;
    private RequestStatus prevStatus;
    private RequestStatus newStatus;
    private RequestStatus managerStatus;

    public RequestPL(ShiftPL shift, String prevEmployee, String newEmployee,
                     String manager, RequestStatus prevStatus, RequestStatus newStatus,
                     RequestStatus managerStatus) {
        this.shift = shift;
        this.prevEmployee = prevEmployee;
        this.newEmployee = newEmployee;
        this.manager = manager;
        this.prevStatus = prevStatus;
        this.newStatus = newStatus;
        this.managerStatus = managerStatus;
    }

    public RequestPL(RequestSL request) {
        this(new ShiftPL(request.getShift()),
                request.getPrevEmployee(),
                request.getNewEmployee(),
                request.getManager(),
                request.getPrevStatus(),
                request.getNewStatus(),
                request.getManagerStatus()
        );
    }

    public ShiftPL getShift() {
        return shift;
    }

    public RequestSL toRequest() {
        return new RequestSL(RequestSL.NO_ID,
                shift.toShift(), prevEmployee, newEmployee, manager,
                prevStatus, newStatus, managerStatus
        );
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Day: ").append(shift.getDay().day)
                .append(" At: ").append(shift.getShiftType().type).append("\n");

        if (managerStatus == RequestStatus.PENDING) {
            s.append("Pending For Manager Review: ")
                    .append(prevEmployee).append(" (").append(prevStatus).append(")")
                    .append(" ==> ")
                    .append(newEmployee).append(" (").append(newStatus).append(")");
        } else {
            s.append(manager).append(" ").append(managerStatus).append(" ").append("Request")
                    .append(prevEmployee).append(" (").append(prevStatus).append(")")
                    .append(" ==> ")
                    .append(newEmployee).append(" (").append(newStatus).append(")");
        }

        return s.toString();
    }
}
