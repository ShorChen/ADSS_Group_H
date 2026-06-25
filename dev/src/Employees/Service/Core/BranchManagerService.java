package Employees.Service.Core;

import Core.Service.Response;
import Employees.Domain.Facades.EmployeesFacade;
import Employees.Domain.Entities.*;
import Employees.Service.DTO.*;
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