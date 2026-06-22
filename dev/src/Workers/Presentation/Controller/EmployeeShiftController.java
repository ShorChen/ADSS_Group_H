package Workers.Presentation.Controller;

import Workers.Context.SessionManager;
import Workers.Presentation.DTO.AvailabilitySubmissionPL;
import Workers.Service.EmployeeService;

public class EmployeeShiftController {
    private final EmployeeService employeeService;

    public EmployeeShiftController(){
        this.employeeService = new EmployeeService();
    }
    public void setCurrentEmployeeShiftsAsUnavailable(AvailabilitySubmissionPL availabilitySub){

        employeeService.updateAvailability(availabilitySub.toAvailabilitySubmission(
                SessionManager.getCurrentEmployee().toEmployee().getId()));
    }

}
