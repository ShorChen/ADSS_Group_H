package Employees.Service.Core;

import Core.Service.Response;
import Employees.Domain.Facades.EmployeesFacade;
import Employees.Domain.Entities.*;
import Employees.Service.DTO.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BranchManagerService {
    private final EmployeesFacade facade;

    public BranchManagerService(EmployeesFacade facade) { this.facade = facade; }

    public Response<String> saveShift(ShiftDL shiftDL) {
        try {
            facade.addUpdateShift(shiftDL);
            return new Response<>("Shift saved.");
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<List<ShiftSL>> getBranchShifts(int branchId, int year, int week) {
        try {
            List<ShiftSL> list = facade.getShiftsByBranchAndWeek(branchId, year, week).stream()
                    .map(s -> new ShiftSL(s.getShiftId(), s.getBranchId(), s.getYear(), s.getWeek(), s.getStartDate(), s.getDay().name(), s.getShiftType().name()))
                    .collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    /** Returns ALL shifts for a branch (for the "Past Shifts" manager view). */
    public Response<List<ShiftSL>> getPastShifts(int branchId) {
        try {
            List<ShiftSL> list = facade.getShiftsByBranch(branchId).stream()
                    .map(s -> new ShiftSL(s.getShiftId(), s.getBranchId(), s.getYear(), s.getWeek(), s.getStartDate(), s.getDay().name(), s.getShiftType().name()))
                    .collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    /**
     * Sets the availability-submission deadline for a branch/year/week.
     * The deadline date must be at least 1 day in the future.
     */
    public Response<String> setAvailabilityDeadline(int branchId, int year, int week, LocalDate deadline) {
        try {
            if (!deadline.isAfter(LocalDate.now())) {
                return new Response<>("Deadline must be at least 1 day in the future (today is " + LocalDate.now() + ")");
            }
            facade.saveDeadline(branchId, year, week, deadline.toString());
            return new Response<>("Deadline set to " + deadline + " for Year " + year + " Week " + week);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    /**
     * Returns whether the availability-submission window is still open
     * (current date is on or before the stored deadline).
     */
    public Response<Boolean> isAvailabilitySubmissionOpen(int branchId, int year, int week) {
        try {
            String deadlineStr = facade.getDeadline(branchId, year, week);
            if (deadlineStr == null) return new Response<>(true); // no deadline = always open
            LocalDate deadline = LocalDate.parse(deadlineStr);
            return new Response<>(!LocalDate.now().isAfter(deadline));
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    /**
     * Returns the stored deadline for a branch/year/week, or null if none is set.
     */
    public Response<String> getDeadline(int branchId, int year, int week) {
        try {
            return new Response<>(facade.getDeadline(branchId, year, week));
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    /**
     * Checks whether an employee has declared themselves available for a given day+shift.
     * If no availability was submitted the employee is assumed to be available.
     */
    public Response<Boolean> isEmployeeAvailableForShift(String employeeId, String day, String shiftType) {
        try {
            EmployeeDL emp = facade.getEmployee(employeeId);
            if (emp == null) throw new RuntimeException("Employee " + employeeId + " not found.");
            AvailabilitySubmissionDL avail = emp.getAvailabilitySubmission();
            // No submission → available for all shifts
            if (avail == null || avail.getShifts().isEmpty()) return new Response<>(true);
            String key = day.toUpperCase() + "_" + shiftType.toUpperCase();
            return new Response<>(avail.getShift(key)); // defaults to true if key absent
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    // --- INTEGRATION API FOR TRANSPORTATION MODULE ---

    public boolean isEmployeeWorking(int branchId, int year, int week, Employees.Shared.Enums.WeekDay day, Employees.Shared.Enums.ShiftType shiftType, String employeeId) {
        List<ShiftDL> shifts = facade.getShiftsByBranchAndWeek(branchId, year, week);
        for (ShiftDL shift : shifts) {
            if (shift.getDay() == day && shift.getShiftType() == shiftType) {
                return shift.doesEmployeeWork(employeeId);
            }
        }
        return false;
    }

    public boolean isRoleAssigned(int branchId, int year, int week, Employees.Shared.Enums.WeekDay day, Employees.Shared.Enums.ShiftType shiftType, String roleName) {
        List<ShiftDL> shifts = facade.getShiftsByBranchAndWeek(branchId, year, week);
        for (ShiftDL shift : shifts) {
            if (shift.getDay() == day && shift.getShiftType() == shiftType) {
                for (java.util.Map.Entry<RoleDL, java.util.Set<String>> entry : shift.getEmployees().entrySet()) {
                    if (entry.getKey().getTag().equalsIgnoreCase(roleName) && !entry.getValue().isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}