package presentation.control;

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
}
