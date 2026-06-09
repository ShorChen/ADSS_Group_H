package domain.services;

import data_access.pools.BranchPool;
import presentation.model.BranchPL;

import java.util.ArrayList;
import java.util.List;

public class BranchService {
    private final BranchPool branchPool;

    public BranchService() {
        branchPool = BranchPool.Instance();
    }

    public List<BranchPL> getBranches() {
        List<BranchPL> branches = new ArrayList<>();
        branchPool.getAllBranches().forEach(branch ->
                branches.add(new BranchPL(branch)));
        return branches;
    }
}
