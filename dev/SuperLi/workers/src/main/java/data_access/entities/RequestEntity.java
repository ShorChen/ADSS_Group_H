package data_access.entities;

import domain.entities.Shift;

public class RequestEntity {
    private long id;
    private Shift shift;
    private String prevEmployee;
    private String newEmployee;
    private String manager;
    private boolean prevApproved;
    private boolean newApproved;
    private boolean managerApproved;
    private boolean denied;

    public RequestEntity(long id, Shift shift, String prevEmployee,
                         String newEmployee, String manager, boolean prevApproved,
                         boolean newApproved, boolean managerApproved,
                         boolean denied) {
        this.id = id;
        this.shift = shift;
        this.prevEmployee = prevEmployee;
        this.newEmployee = newEmployee;
        this.manager = manager;
        this.prevApproved = prevApproved;
        this.newApproved = newApproved;
        this.managerApproved = managerApproved;
        this.denied = denied;
    }

    public long getId() {
        return id;
    }

    public Shift getShift() {
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
}
