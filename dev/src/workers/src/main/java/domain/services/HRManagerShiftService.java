package domain.services;

import data_access.entities.keys.BranchWeekKey;
import data_access.pools.ShiftPool;
import domain.entities.Role;
import domain.entities.Shift;
import shared.enums.WeekDay;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class HRManagerShiftService {
    private ShiftPool shiftPool;

    public HRManagerShiftService() {
        this.shiftPool = ShiftPool.Instance(); 
    }

//    public void setJobsToShift(BranchWeekKey key, Shift shift, Map<Role, Integer> jobs) {
//        if (shift == null || jobs == null) {
//            throw new IllegalArgumentException("Shift or jobs cannot be null");
//        }
//        jobs.forEach(shift::addRole);
//        shiftPool.addUpdateShift(key, shift.toEntity());
//    }

 
//    public List<String> placeToShifts(List<EmployeePL> employees, List<Shift> shifts) {
//        List<String> warnings = new ArrayList<>();
//
//        if (employees == null || shifts == null) return warnings;
//
//        for (Shift shift : shifts) {
//            Map<Role, Integer> required = shift.getRequiredJobs();
//            if (required == null || required.isEmpty()) continue;
//
//            WeekDay shiftDay = getWeekDayFromDate(shift.getStartDate());
//            ShiftType shiftType = shift.getShiftType();
//
//            // 2. עוברים על כל תפקיד שחסר במשמרת
//            for (Map.Entry<Role, Integer> entry : required.entrySet()) {
//                Role neededRole = entry.getKey();
//                int amountNeeded = entry.getValue();
//                int assignedCount = 0;
//
//                for (Employee emp : employees) {
//                    if (assignedCount >= amountNeeded) break;
//
//                    if (!emp.isActive()) continue;
//
//                    if (shift.getEmployees().containsKey(emp)) continue;
//
//                    if (!emp.getQualifiedRoles().contains(neededRole)) continue;
//
//                    boolean isUnavailable = false;
//                    if (emp.getUnavailableShifts().containsKey(shiftDay)) {
//                        if (emp.getUnavailableShifts().get(shiftDay).contains(shiftType)) {
//                            isUnavailable = true;
//                        }
//                    }
//                    if (isUnavailable) continue;
//
//
//                    shift.getEmployees().put(emp, neededRole);
//                    assignedCount++;
//                }
//
//                if (assignedCount < amountNeeded) {
//                    int missingAmount = amountNeeded - assignedCount;
//                    warnings.add("Warning: Shift on " + shift.getStartDate() +
//                                 " is missing " + missingAmount +
//                                 " employees for role: " + neededRole.getTag());
//                }
//            }
//        }
//
//        return warnings;
//    }

    //helper function to convert Date to WeekDay Enum
    private WeekDay getWeekDayFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayNum = cal.get(Calendar.DAY_OF_WEEK) - 1; 
        return WeekDay.values()[dayNum];
    }
}