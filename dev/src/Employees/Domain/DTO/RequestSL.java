package Employees.Domain.DTO;

import Employees.DataAccess.Entities.Keys.BranchWeekKey;
import Employees.DataAccess.Entities.RequestEntity;
import Employees.Shared.Enums.RequestStatus;

import java.util.Objects;

public class RequestSL {
    public static final int NO_ID = -1;
    private int requestId;
    private ShiftSL shift;
    private int branchId;
    private int year;
    private int week;
    private String prevEmployee;
    private String newEmployee;
    private String manager;
    private RoleSL role;
    private RequestStatus prevStatus;
    private RequestStatus newStatus;
    private RequestStatus managerStatus;

    public RequestSL(ShiftSL shift, String prevEmployee, String newEmployee) {
        this(NO_ID, shift, prevEmployee, newEmployee, "", RequestStatus.ACCEPT,
                RequestStatus.PENDING, RequestStatus.PENDING);
    }

    public RequestSL(int requestId, ShiftSL shift, String prevEmployee, String newEmployee,
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
        return new RequestEntity(requestId, shift.toEntity(),new BranchWeekKey(branchId, year, week), prevEmployee,
                newEmployee, manager, prevStatus.toString(),
                newStatus.toString(), managerStatus.toString(),role.getTag(), isDenied());
    }

    public RequestSL(RequestEntity entity) {
        this(
                entity.requestId(), new ShiftSL(entity.shift()),
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

    public ShiftSL getShift() {
        return new ShiftSL(shift);
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

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setShift(ShiftSL shift) {
        this.shift = shift;
    }

    public void setPrevEmployee(String prevEmployee) {
        this.prevEmployee = prevEmployee;
    }

    public void setNewEmployee(String newEmployee) {
        this.newEmployee = newEmployee;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setPrevStatus(RequestStatus prevStatus) {
        this.prevStatus = prevStatus;
    }

    public void setNewStatus(RequestStatus newStatus) {
        this.newStatus = newStatus;
    }

    public void setManagerStatus(RequestStatus managerStatus) {
        this.managerStatus = managerStatus;
    }

    public int getBranchId() {
        return branchId;
    }

    public int getYear() {
        return year;
    }

    public int getWeek() {
        return week;
    }

    public RoleSL getRole() {
        return role;
    }
}
