package Employees.Presentation.Controller;

import Employees.Context.SessionManager;
import Employees.Domain.Entities.ShiftKey;
import org.jetbrains.annotations.NotNull;
import Employees.Service.EmployeeService;

import java.util.Set;

public class EmployeeShiftController {
    private final EmployeeService employeeService;

    public EmployeeShiftController(){
        this.employeeService = new EmployeeService();
    }
    public void setCurrentEmployeeShiftsAsUnavailable(@NotNull Set<ShiftKey> shifts,
                                                      boolean canWorkDoubleShifts){
        employeeService.updateAvailability(SessionManager.getCurrentEmployee().getId(),
                shifts, canWorkDoubleShifts);
    }

}
