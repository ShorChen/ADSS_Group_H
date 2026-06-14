package domain.services;

import data_access.pools.BranchPool;
import domain.entities.store.Branch;
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

    public void addUpdateBranch(Branch branch) {
        branchPool.addUpdateBranch(branch.toEntity());
    }

    public boolean containsBranchAtLocation(String location) {
        return branchPool.getAllBranchLocations().contains(location);
    }
}
