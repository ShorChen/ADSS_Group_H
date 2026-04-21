package domain.entities;

import data_access.entities.RequestEntity;

import java.util.Objects;

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

    public RequestEntity toEntity() {
        return new RequestEntity(shift.toEntity(), prevEmployee, newEmployee, manager,
                prevApproved, newApproved, managerApproved, denied);
    }

    public Request(RequestEntity entity) {
        this(
                new Shift(entity.getShift()),
                entity.getPrevEmployee(), entity.getNewEmployee(), entity.getManager(),
                entity.isPrevApproved(), entity.isNewApproved(),
                entity.isManagerApproved(), entity.isDenied());
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

    public boolean approve(String id) {
        if (denied) return false;
        if (Objects.equals(id, prevEmployee)) {
            prevApproved = true;
            return true;
        }
        if (Objects.equals(id, newEmployee)) {
            newApproved = true;
        }
        if (manager == null) {
            managerApproved = true;
        }
        return false;

    }

    public boolean deny(String id) {
        if (isApproved() || denied) return false;
        if (Objects.equals(id, prevEmployee)) {
            prevApproved = false;
            denied = true;
            return true;
        }
        if (Objects.equals(id, newEmployee)) {
            newApproved = false;
            denied = true;
            return true;

        }
        if (manager == null) {
            managerApproved = false;
            denied = true;
            return true;
        }
        return false;
    }

    public boolean isApproved() {
        return prevApproved && (newApproved || newEmployee == null) &&
               managerApproved && !denied;
    }
}
