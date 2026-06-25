package Employees.Presentation.Controller;

import Employees.Service.Core.BranchManagerService;
import Employees.Service.DTO.*;
import Employees.Presentation.DTO.*;
import Core.Service.Response;
import Employees.Domain.Entities.ShiftDL;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BranchManagerController {
    private final BranchManagerService service;

    public BranchManagerController(BranchManagerService service) { this.service = service; }

    public void saveShift(ShiftDL shift) {
        Response<String> res = service.saveShift(shift);
        if (!res.isSuccess()) throw new RuntimeException(res.getErrorMessage());
    }

    public List<ShiftPL> getBranchShifts(int branchId, int year, int week) {
        Response<List<ShiftSL>> res = service.getBranchShifts(branchId, year, week);
        if (!res.isSuccess()) throw new RuntimeException(res.getErrorMessage());
        return res.getData().stream()
                .map(sl -> new ShiftPL(sl.shiftId(), sl.branchId(), sl.year(), sl.week(), sl.startDate(), sl.day(), sl.shiftType()))
                .collect(Collectors.toList());
    }

    /** Returns all shifts ever recorded for a branch (for the Past Shifts view). */
    public List<ShiftPL> getPastShifts(int branchId) {
        Response<List<ShiftSL>> res = service.getPastShifts(branchId);
        if (!res.isSuccess()) throw new RuntimeException(res.getErrorMessage());
        return res.getData().stream()
                .map(sl -> new ShiftPL(sl.shiftId(), sl.branchId(), sl.year(), sl.week(), sl.startDate(), sl.day(), sl.shiftType()))
                .collect(Collectors.toList());
    }

    /**
     * Sets the availability deadline. Throws if the date is not at least 1 day in the future.
     */
    public void setAvailabilityDeadline(int branchId, int year, int week, LocalDate deadline) {
        Response<String> res = service.setAvailabilityDeadline(branchId, year, week, deadline);
        if (!res.isSuccess()) throw new RuntimeException(res.getErrorMessage());
    }

    /** Returns whether employees can still submit availability for this week. */
    public boolean isAvailabilitySubmissionOpen(int branchId, int year, int week) {
        Response<Boolean> res = service.isAvailabilitySubmissionOpen(branchId, year, week);
        if (!res.isSuccess()) return true; // assume open on error
        return Boolean.TRUE.equals(res.getData());
    }

    /** Returns the stored deadline ISO string, or null if none set. */
    public String getDeadline(int branchId, int year, int week) {
        Response<String> res = service.getDeadline(branchId, year, week);
        return res.isSuccess() ? res.getData() : null;
    }

    /**
     * Returns true if the employee is available for the given shift.
     * If the employee submitted no availability, they are assumed available.
     */
    public boolean isEmployeeAvailableForShift(String employeeId, String day, String shiftType) {
        Response<Boolean> res = service.isEmployeeAvailableForShift(employeeId, day, shiftType);
        if (!res.isSuccess()) return true; // assume available on error
        return Boolean.TRUE.equals(res.getData());
    }

    public ShiftDL getShift(int branchId, int year, int week, String day, String shiftType) {
        Response<List<Employees.Service.DTO.ShiftSL>> res = service.getBranchShifts(branchId, year, week);
        if (!res.isSuccess()) throw new RuntimeException(res.getErrorMessage());
        for (Employees.Service.DTO.ShiftSL sl : res.getData()) {
            if (sl.day().equals(day) && sl.shiftType().equals(shiftType)) {
                return new ShiftDL(sl.shiftId(), sl.branchId(), sl.year(), sl.week(), sl.startDate(), Employees.Shared.Enums.WeekDay.valueOf(sl.day()), Employees.Shared.Enums.ShiftType.valueOf(sl.shiftType()), new java.util.HashMap<>(), new java.util.HashMap<>());
            }
        }
        throw new RuntimeException("Shift not found");
    }
}