package Employees.Service;

import Employees.DataAccess.BranchDAO;
import Employees.DataAccess.Entities.Keys.WeekKey;
import Employees.DataAccess.SqlImpl.SqlBranchDAO;
import Employees.Domain.DTO.BranchSL;

import java.util.ArrayList;
import java.util.List;

public class BranchService {
    private final BranchDAO branchDAO;

    public BranchService() {
        branchDAO = new SqlBranchDAO();
    }

    public List<BranchSL> getBranches() {
        List<BranchSL> branches = new ArrayList<>();
        branchDAO.getAllBranches().forEach(branch ->
                branches.add(new BranchSL(branch)));
        return branches;
    }

    public void addUpdateBranch(BranchSL branch) {
        branchDAO.addUpdateBranch(branch.toEntity());
    }

    public boolean containsBranchAtLocation(String location) {
        return branchDAO.getAllBranchLocations().contains(location);
    }

    public boolean isFirstWeek(int branchId, int year, int week) {
        return branchDAO.get(branchId).startDate().equals(new WeekKey(year, week));
    }
}
