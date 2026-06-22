package Workers.DataAccess.DAO;

import Workers.DataAccess.Entities.Keys.BranchWeekKey;
import Workers.DataAccess.Entities.Keys.ShiftEntityKey;
import Workers.DataAccess.Entities.ShiftEntity;

import java.util.List;

public interface ShiftDAO {
    List<ShiftEntity> getShiftsByBranchAndWeek(int branchId, int year, int week);

    void addUpdateShift(BranchWeekKey branchWeekKey, ShiftEntityKey shiftEntityKey,
                               ShiftEntity shift);

    void removeShift(int branchId, int year, int week, String day, String type);
}
