package Employees.Domain.Entities;

import Employees.DataAccess.Entities.RequestEntity;
import Employees.Shared.Enums.RequestStatus;

import java.util.Objects;

public class Request {
    public static final int NO_ID = -1;
    private int requestId;
    private Shift shift;
    private String prevEmployee;
    private String newEmployee;
    private String manager;
    private RequestStatus prevStatus;
    private RequestStatus newStatus;
    private RequestStatus managerStatus;

    public Request(Shift shift, String prevEmployee, String newEmployee) {
        this(NO_ID, shift, prevEmployee, newEmployee, "", RequestStatus.ACCEPT,
                RequestStatus.PENDING, RequestStatus.PENDING);
    }

    public Request(int requestId, Shift shift, String prevEmployee, String newEmployee,
                   String manager, RequestStatus prevStatus, RequestStatus newStatus,
                   RequestStatus managerStatus) {
        this.requestId = requestId;
        this.shift = shift;
        this.prevEmployee = prevEmployee;
        this.newEmployee = newEmployee;
        this.manager = manager;
        this.prevStatus = prevStatus;
        this.newStatus = newStatus;
        this.managerStatus = managerStatus;
    }

    public RequestEntity toEntity() {
        return new RequestEntity(requestId, shift.toEntity(), prevEmployee,
                newEmployee, manager, prevStatus.toString(),
                newStatus.toString(), managerStatus.toString(), isDenied());
    }

    public Request(RequestEntity entity) {
        this(
                entity.requestId(), new Shift(entity.shift()),
                entity.prevEmployee(), entity.newEmployee(), entity.manager(),
                RequestStatus.fromArgs(entity.prevApproved()),
                RequestStatus.fromArgs(entity.newApproved()),
                RequestStatus.fromArgs(entity.managerApproved())
        );
    }

    public void approve(String id) {
        if (!isDenied()) {
            if (Objects.equals(id, prevEmployee)) {
                prevStatus = RequestStatus.ACCEPT;
            }
            if (Objects.equals(id, newEmployee)) {
                newStatus = RequestStatus.ACCEPT;
            }
            if (manager.isBlank()) {
                managerStatus = RequestStatus.ACCEPT;
                manager = id;
            }
        }

    }

    public void deny(String id) {
        if (!isApproved()) {
            if (Objects.equals(id, prevEmployee)) {
                prevStatus = RequestStatus.REJECT;
            }
            if (Objects.equals(id, newEmployee)) {
                newStatus = RequestStatus.REJECT;

            }
            if (manager.isBlank()) {
                managerStatus = RequestStatus.REJECT;
                manager = id;
            }
        }

    }

    public boolean isDenied() {
        return prevStatus == RequestStatus.REJECT ||
               newStatus == RequestStatus.REJECT ||
               managerStatus == RequestStatus.REJECT;
    }

    public boolean isApproved() {
        return prevStatus == RequestStatus.ACCEPT &&
               newStatus == RequestStatus.ACCEPT &&
               managerStatus == RequestStatus.ACCEPT;
    }

    public Shift getShift() {
        return new Shift(shift);
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

    public RequestStatus getPrevStatus() {
        return prevStatus;
    }

    public RequestStatus getNewStatus() {
        return newStatus;
    }

    public RequestStatus getManagerStatus() {
        return managerStatus;
    }

    public int getRequestId() {
        return requestId;
    }
}
