package Employees.Presentation.Controller;

import Employees.Service.Core.BranchManagerService;
import Employees.Service.DTO.*;
import Employees.Presentation.DTO.*;
import Core.Service.Response;
import Employees.Domain.Entities.ShiftDL;
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
}