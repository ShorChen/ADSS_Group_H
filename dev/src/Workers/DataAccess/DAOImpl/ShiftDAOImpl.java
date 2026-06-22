package Workers.DataAccess.DAOImpl;

import Workers.DataAccess.DAO.ShiftDAO;
import Workers.DataAccess.Entities.Keys.BranchWeekKey;
import Workers.DataAccess.Entities.Keys.ShiftEntityKey;
import Workers.DataAccess.Entities.ShiftEntity;

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
