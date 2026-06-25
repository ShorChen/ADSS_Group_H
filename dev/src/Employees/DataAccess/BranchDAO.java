package Employees.DataAccess;
import Employees.Domain.Entities.BranchDL;
import java.util.List;

public interface BranchDAO {
    List<BranchDL> getAllBranches();
    void addUpdateBranch(BranchDL branch);
}