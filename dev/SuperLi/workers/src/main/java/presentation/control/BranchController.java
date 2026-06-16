package presentation.control;

import domain.entities.store.Branch;
import domain.services.BranchService;
import presentation.model.BranchPL;

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
        service.addUpdateBranch(new Branch(-1, location));
    }

    public boolean containsBranch(String location) {
        return service.containsBranchAtLocation(location);
    }
}
