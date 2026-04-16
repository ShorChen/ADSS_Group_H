package presentation.control;

import domain.entities.ExceptionalPlacementRequest;
import domain.entities.ReplacementRequest;
import domain.entities.Shift;
import domain.services.ShiftService;

import java.util.List;
import java.util.Map;

/* requirement no. 4, 21 22 24 26 27 28*/
public class EmployeeShiftController {
    private ShiftService service;
    public EmployeeShiftController(ShiftService service){
        this.service = service;
    }
    public void setAvailability(String employeeId, List<Shift> shifts, boolean canWorkDoubleShifts){
        //todo: implement
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
