package Employees.Service;

import Employees.DataAccess.BranchDAO;
import Employees.DataAccess.SqlImpl.SqlBranchDAO;
import Employees.Domain.Entities.Store.Branch;
import Employees.Presentation.Model.BranchPL;

import java.util.ArrayList;
import java.util.List;

public class BranchService {
    private final BranchDAO branchDAO;

    public BranchService() {
        branchDAO = new SqlBranchDAO();
    }

    public List<BranchPL> getBranches() {
        List<BranchPL> branches = new ArrayList<>();
        branchDAO.getAllBranches().forEach(branch ->
                branches.add(new BranchPL(branch)));
        return branches;
    }

    public void addUpdateBranch(Branch branch) {
        branchDAO.addUpdateBranch(branch.toEntity());
    }

    public boolean containsBranchAtLocation(String location) {
        return branchDAO.getAllBranchLocations().contains(location);
    }
}