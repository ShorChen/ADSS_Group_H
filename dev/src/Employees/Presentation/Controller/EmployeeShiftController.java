package Employees.Presentation.Controller;

import Employees.Context.SessionManager;
import Employees.Presentation.DTO.AvailabilitySubmissionPL;
import Employees.Service.EmployeeService;
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
