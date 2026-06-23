package Employees.DataAccess.DAOImpl;

import Employees.DataAccess.BranchDAO;
import Employees.DataAccess.Entities.BranchEntity;

import java.util.List;

public class BranchDAOImpl implements BranchDAO {
    @Override
    public List<String> getAllBranchLocations() {
        return List.of();
    }

    @Override
    public boolean exists(int branchId) {
        return false;
    }

    @Override
    public List<BranchEntity> getAllBranches() {
        return List.of();
    }

    @Override
    public void addUpdateBranch(BranchEntity branch) {

    }

    @Override
    public BranchEntity get(int branchId) {
        return null;
    }
}
