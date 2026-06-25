package Employees.Domain.Entities;

public class RequestDL {
    private int requestId;
    private ShiftDL shift;
    private String prevEmployeeId;
    private String newEmployeeId;
    private String managerId;
    private String prevApproved;
    private String newApproved;
    private String managerApproved;
    private boolean denied;

    public RequestDL(int requestId, ShiftDL shift, String prevEmployeeId, String newEmployeeId, String managerId,
                     String prevApproved, String newApproved, String managerApproved, boolean denied) {
        this.requestId = requestId;
        this.shift = shift;
        this.prevEmployeeId = prevEmployeeId;
        this.newEmployeeId = newEmployeeId;
        this.managerId = managerId;
        this.prevApproved = prevApproved;
        this.newApproved = newApproved;
        this.managerApproved = managerApproved;
        this.denied = denied;
    }

    public int getRequestId() { return requestId; }
    public ShiftDL getShift() { return shift; }

    public String getPrevEmployeeId() { return prevEmployeeId; }
    public String getNewEmployeeId() { return newEmployeeId; }
    public String getManagerId() { return managerId; }

    public String getPrevApproved() { return prevApproved; }
    public String getNewApproved() { return newApproved; }
    public String getManagerApproved() { return managerApproved; }

    public boolean isDenied() { return denied; }
}