package Workers.Presentation.Controller;

import Workers.Context.SessionManager;
import Workers.Domain.DTO.ShiftKey;
import org.jetbrains.annotations.NotNull;
import Workers.Service.EmployeeService;

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
