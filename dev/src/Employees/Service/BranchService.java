package Employees.Service;

import Employees.DataAccess.BranchDAO;
import Employees.DataAccess.Entities.Keys.WeekKey;
import Employees.DataAccess.Pools.BranchPool;
import Employees.Domain.DTO.BranchSL;

import java.util.ArrayList;
import java.util.List;

public class BranchService {
    private final BranchDAO branchPool;

    public BranchService() {
        branchPool = BranchPool.Instance();
    }

    public List<BranchSL> getBranches() {
        List<BranchSL> branches = new ArrayList<>();
        branchPool.getAllBranches().forEach(branch ->
                branches.add(new BranchSL(branch)));
        return branches;
    }

    public void addUpdateBranch(BranchSL branch) {
        branchPool.addUpdateBranch(branch.toEntity());
    }

    public boolean containsBranchAtLocation(String location) {
        return branchPool.getAllBranchLocations().contains(location);
    }

    public boolean isFirstWeek(int branchId, int year, int week) {
        return branchPool.get(branchId).startDate().equals(new WeekKey(year, week));
    }
}
