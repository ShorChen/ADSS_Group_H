package Employees.Presentation.Controller;

import Employees.Context.SessionManager;
import Employees.Domain.Entities.Store.Branch;
import Employees.Service.BranchService;
import Employees.Presentation.Model.BranchPL;

import java.util.List;

public class BranchController {
    private final BranchService service;

    public BranchController() {
        service = new BranchService();
    }

    public List<BranchPL> getBranches() {
        return service.getBranches();
    }

    public void addBranch(String location) {
        service.addUpdateBranch(new Branch(-1,
                SessionManager.getCurrentEmployee().getId()
                ,location));
    }

    public boolean containsBranch(String location) {
        return service.containsBranchAtLocation(location);
    }
}
