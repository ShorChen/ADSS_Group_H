package presentation.model;

import domain.entities.Request;

import java.util.Objects;

public class RequestPL {

    private ShiftPL shift;
    private String prevEmployee;
    private String newEmployee;
    private String manager;
    private boolean prevApproved;
    private boolean newApproved;
    private boolean managerApproved;
    private boolean denied;

    public boolean approve(String id) {
        if (denied) return false;
        if (Objects.equals(id, prevEmployee)) {
            prevApproved = true;
            return true;
        }
        if (Objects.equals(id, newEmployee)) {
            newApproved = true;
        }
        if (Objects.equals(id, manager)) {
            managerApproved = true;
        }
        return false;


    }

    public boolean deny(String id) {
        if (isApproved() || !(Objects.equals(id, prevEmployee) ||
                              Objects.equals(id, newEmployee) ||
                              Objects.equals(id, manager))) return false;

        denied = true;
        return true;
    }

    public boolean isPrevApproved() {
        return prevApproved;
    }

    public boolean isApproved() {
        return prevApproved && (newApproved || newEmployee == null) &&
               managerApproved && !denied;
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
}
