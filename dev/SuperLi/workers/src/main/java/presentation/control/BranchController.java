package presentation.control;

import context.SessionManager;
import domain.entities.store.Branch;
import domain.services.BranchService;
import presentation.model.BranchPL;

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
