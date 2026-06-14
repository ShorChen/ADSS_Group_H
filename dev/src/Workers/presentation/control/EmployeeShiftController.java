package Workers.presentation.control;

import Workers.context.SessionManager;
import Workers.domain.entities.ShiftKey;
import org.jetbrains.annotations.NotNull;
import Workers.domain.services.EmployeeService;

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
