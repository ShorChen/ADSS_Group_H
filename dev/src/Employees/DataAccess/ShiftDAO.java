package Employees.DataAccess;
import Employees.Domain.Entities.ShiftDL;
import java.util.List;

public interface ShiftDAO {
    void addUpdateShift(ShiftDL shift);
    ShiftDL getShiftById(int shiftId);
    List<ShiftDL> getShiftsByBranchAndWeek(int branchId, int year, int week);
}