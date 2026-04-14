package presentation;

import domain.Shift;
import domain.ShiftFacade;

import java.util.List;

public class EmployeeShiftService {
    private ShiftFacade facade;
    public EmployeeShiftService(ShiftFacade facade){
        this.facade = facade;
    }
    public void setConstraints(List<Shift> shifts, boolean canWorkDoubleShifts){}
    public void setPreferredRestDay(String day){}

}
