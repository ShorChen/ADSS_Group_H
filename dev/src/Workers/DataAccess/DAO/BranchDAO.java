package Workers.DataAccess.DAO;

import Workers.DataAccess.Entities.BranchEntity;

import java.util.List;

public interface BranchDAO {
    List<String> getAllBranchLocations();

    boolean exists(int branchId);

    List<BranchEntity> getAllBranches();

    void addUpdateBranch(BranchEntity branch);

    BranchEntity get(int branchId);
}
