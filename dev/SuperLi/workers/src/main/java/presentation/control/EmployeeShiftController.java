package presentation.control;

import context.SessionManager;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import domain.services.EmployeeService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EmployeeShiftController {
    private final EmployeeService employeeService;

    public EmployeeShiftController(){
        this.employeeService = new EmployeeService();
    }
    public void setCurrentEmployeeShiftsAsUnavailable(Map<Integer, Set<Integer>> shifts,
                                                      boolean canWorkDoubleShifts){
        Map<WeekDay, Set<ShiftType>> domainShifts = new HashMap<>();
        if (shifts != null) {
            shifts.forEach((dayInt, shiftInts) -> {
                WeekDay day = WeekDay.values()[dayInt];
                Set<ShiftType> types = new HashSet<>();
                
                for (Integer shiftInt : shiftInts) {
                    types.add(ShiftType.values()[shiftInt]);
                }
                
                domainShifts.put(day, types);
            });
        }
        employeeService.updateAvailability(SessionManager.getCurrentEmployee().getId(),
                domainShifts, canWorkDoubleShifts);
    }

}
