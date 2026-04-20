package presentation.control;

import domain.entities.ExceptionalPlacementRequest;
import domain.entities.ReplacementRequest;
import domain.entities.Shift;
import domain.services.ShiftService;
import domain.services.EmployeeService;
import domain.enums.WeekDay;
import domain.enums.ShiftType;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

/* requirement no. 4, 21 22 24 26 27 28*/
public class EmployeeShiftController {
    private ShiftService service;
    private EmployeeService employeeService;

    public EmployeeShiftController(){
        this.service = new ShiftService();
        this.employeeService = new EmployeeService();
    }
    public void setAvailability(String employeeId, Map<Integer, Set<Integer>> shifts,
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
        employeeService.updateAvailability(employeeId, domainShifts, canWorkDoubleShifts);
    }
    public void setPreferredRestDay(String day){
        // todo: implement
    }

    public void requestReplacement(ReplacementRequest request){
        // todo
    }

    //day to shifts
    public Map<String, List<Shift>> getShifts(){
        // todo
        return null;
    }

    public void handleExceptionalPlacementRequest(ExceptionalPlacementRequest request, boolean approve) {
    }

}
