package Employees.Service;

import Employees.DataAccess.Entities.Keys.BranchWeekKey;
import Employees.DataAccess.Entities.Keys.ShiftEntityKey;
import Employees.DataAccess.ShiftDAO;
import Employees.DataAccess.SqlImpl.SqlShiftDAO;
import Employees.Domain.Entities.Shift;
import Employees.Domain.Entities.ShiftKey;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ShiftService {
    private final ShiftDAO shiftDAO;

    public ShiftService() {
        shiftDAO = new SqlShiftDAO();
    }

    public Map<ShiftKey, Shift> getShiftsOfWeek(int branchId, int year, int week) {
        Map<ShiftKey, Shift> weekMap = new HashMap<>();
        shiftDAO.getShiftsByBranchAndWeek(branchId, year, week).forEach(shiftEntity -> {
            Shift shift = new Shift(shiftEntity);
            weekMap.put(new ShiftKey(shift.getDay(), shift.getShiftType()), shift);
        });
        return weekMap;
    }

    public void addUpdateShift(int branchId, int year, int week,
                               String day, String type,
                               @NotNull Shift shift) {
        shiftDAO.addUpdateShift(new BranchWeekKey(branchId, year, week),
                new ShiftEntityKey(day, type), shift.toEntity());
    }

    public void closeShift(int branchId, int year, int week,
                           String day, String type) {
        shiftDAO.removeShift(branchId, year, week, day, type);
    }
}