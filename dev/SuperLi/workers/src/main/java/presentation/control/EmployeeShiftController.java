package presentation.control;

import context.SessionManager;
import domain.entities.ShiftKey;
import org.jetbrains.annotations.NotNull;
import shared.enums.ShiftType;
import shared.enums.WeekDay;
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
    public void setCurrentEmployeeShiftsAsUnavailable(@NotNull Set<ShiftKey> shifts,
                                                      boolean canWorkDoubleShifts){
        employeeService.updateAvailability(SessionManager.getCurrentEmployee().getId(),
                shifts, canWorkDoubleShifts);
    }

}
