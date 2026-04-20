package domain.entities;

public class Request {
    private Shift shift;
    private String prevEmployee;
    private String newEmployee;
    private String manager;
    private boolean prevApproved;
    private boolean newApproved;
    private boolean managerApproved;
    private boolean denied;

    public Request(Shift shift, String prevEmployee, String newEmployee) {
        this.shift = shift;
        this.prevEmployee = prevEmployee;
        this.newEmployee = newEmployee;
        prevApproved = true;
    }

    public Request(Shift shift, String prevEmployee, String newEmployee,
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

    public Shift getShifts() {
        return shift;
    }

    public String getPrevEmployee() {
        return prevEmployee;
    }

    public String getNewEmployee() {
        return newEmployee;
    }

    public String getManager() {
        return manager;
    }

    public boolean isDenied() {
        return denied;
    }

    public Shift getShift() {
        return shift;
    }

    public boolean isManagerApproved() {
        return managerApproved;
    }

    public boolean isNewApproved() {
        return newApproved;
    }

    public boolean isPrevApproved() {
        return prevApproved;
    }
}
