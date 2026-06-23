package Employees.DataAccess;

import Employees.DataAccess.Entities.BranchEntity;
import java.util.List;

public interface BranchDAO {
    List<String> getAllBranchLocations();

    boolean exists(int branchId, String location);

    List<BranchEntity> getAllBranches();

    void addUpdateBranch(BranchEntity branch);
}