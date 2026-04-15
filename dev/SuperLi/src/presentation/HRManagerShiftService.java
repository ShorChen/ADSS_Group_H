package presentation;

import domain.Employee;
import domain.Role;
import domain.Shift;
import domain.ShiftFacade;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class HRManagerShiftService {
    private ShiftFacade facade;
    public HRManagerShiftService(ShiftFacade facade){
        this.facade = facade;
    }
    public void placeToShifts(Employee employee, List<Shift> shifts){}
    public void requestEmployee(Employee employee, List<Shift> shifts){}
    public void setJobsToShift(Shift shift, Map<Role, Integer> jobs) {}
    public void setDeadline(Date date){
        facade.setConstraintDeadLine(date);
        System.out.println("Constraint submission deadline set to: " + date.toString());
    }
    
    // set working time of the store
    // set default shift (NTH)
}
