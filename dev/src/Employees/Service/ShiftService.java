package Employees.Service;

import Employees.DataAccess.ShiftDAO;
import Employees.DataAccess.Entities.Keys.BranchWeekKey;
import Employees.DataAccess.Entities.Keys.ShiftEntityKey;
import Employees.Domain.DTO.ShiftSL;
import Employees.Domain.DTO.ShiftKey;
import Employees.DataAccess.SqlImpl.SqlShiftDAO;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ShiftService {

    private final ShiftDAO shiftDAO;

    public ShiftService() {
        shiftDAO = new SqlShiftDAO();
    }

    public Map<ShiftKey, ShiftSL> getShiftsOfWeek(int branchId, int year, int week) {
        Map<ShiftKey, ShiftSL> weekMap = new HashMap<>();
        shiftDAO.getShiftsByBranchAndWeek(branchId, year, week).forEach(shiftEntity -> {
            ShiftSL shift = new ShiftSL(shiftEntity);
            weekMap.put(new ShiftKey(shift.getDay(), shift.getShiftType()), shift);
        });
        return weekMap;
    }

    public void addUpdateShift(int branchId, int year, int week,
                               String day, String type,
                               @NotNull ShiftSL shift) {
        shiftDAO.addUpdateShift(new BranchWeekKey(branchId, year, week),
                new ShiftEntityKey(day, type), shift.toEntity());
    }

    public void closeShift(int branchId, int year, int week,
                           String day, String type) {

        shiftDAO.removeShift(branchId, year, week, day, type);
    }
}
