package Employees.Presentation.Controller;

import Employees.Context.SessionManager;
import Employees.Domain.DTO.BranchSL;
import Employees.Service.BranchService;
import Employees.Presentation.DTO.BranchPL;
import Employees.Shared.WeekConstants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BranchController {
    private final BranchService service;

    public BranchController() {
        service = new BranchService();
    }

    public List<BranchPL> getBranches() {
        List<BranchPL> branches = new ArrayList<>();
        service.getBranches().forEach(b -> branches.add(new BranchPL(b)));
        return branches;
    }

    public void addBranch(String location) {
        LocalDate targetDate = SessionManager.now().toLocalDate();

        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());

        service.addUpdateBranch(new BranchSL(-1, location,
                SessionManager.getCurrentEmployee().getId(), year, week));
    }

    public boolean containsBranch(String location) {
        return service.containsBranchAtLocation(location);
    }
}
