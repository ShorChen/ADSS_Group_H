package Employees.Service;

import Employees.DataAccess.Pools.BranchPool;
import Employees.Domain.Entities.Store.Branch;
import Employees.Presentation.Model.BranchPL;

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
