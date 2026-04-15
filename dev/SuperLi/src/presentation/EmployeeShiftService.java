package presentation;

import domain.Employee;
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

    public void setConstraint(Employee employee, Constraint constraint){
        boolean success = facade.submitConstraint(employee, constraint);
        if (success) {
            System.out.println("Constraint submitted successfully for employee " + employee.getName());
        }
    }
}
