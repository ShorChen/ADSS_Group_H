package Employees.DataAccess;

import Employees.DataAccess.Entities.Keys.BranchWeekKey;
import Employees.DataAccess.Entities.Keys.ShiftEntityKey;
import Employees.DataAccess.Entities.ShiftEntity;

import java.util.List;

public interface ShiftDAO {
    List<ShiftEntity> getShiftsByBranchAndWeek(int branchId, int year, int week);

    void addUpdateShift(BranchWeekKey branchWeekKey, ShiftEntityKey shiftEntityKey, ShiftEntity shift);

    void removeShift(int branchId, int year, int week, String day, String type);

    ShiftEntity getShiftById(int shiftId);
}
