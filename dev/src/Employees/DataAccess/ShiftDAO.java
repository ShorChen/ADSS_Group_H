package Employees.DataAccess;
import Employees.Domain.Entities.ShiftDL;
import java.util.List;

public interface ShiftDAO {
    void addUpdateShift(ShiftDL shift);
    ShiftDL getShiftById(int shiftId);
    List<ShiftDL> getShiftsByBranchAndWeek(int branchId, int year, int week);
    /** Returns all shifts for a branch (for past-shifts view), ordered newest first. */
    List<ShiftDL> getShiftsByBranch(int branchId);
    /** Persists the availability-submission deadline for a given branch/year/week. */
    void saveDeadline(int branchId, int year, int week, String deadline);
    /** Returns the deadline ISO-date string for a given branch/year/week, or null if not set. */
    String getDeadline(int branchId, int year, int week);
}