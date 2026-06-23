package Employees.DataAccess.DAOImpl;

import Employees.DataAccess.ShiftDAO;
import Employees.DataAccess.Entities.Keys.BranchWeekKey;
import Employees.DataAccess.Entities.Keys.ShiftEntityKey;
import Employees.DataAccess.Entities.ShiftEntity;

import java.util.List;

public class ShiftDAOImpl implements ShiftDAO {
    @Override
    public List<ShiftEntity> getShiftsByBranchAndWeek(int branchId, int year, int week) {
        return List.of();
    }

    @Override
    public void addUpdateShift(BranchWeekKey branchWeekKey, ShiftEntityKey shiftEntityKey, ShiftEntity shift) {

    }

    @Override
    public void removeShift(int branchId, int year, int week, String day, String type) {

    }
}
