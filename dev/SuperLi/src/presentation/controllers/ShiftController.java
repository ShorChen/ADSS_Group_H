package presentation.controllers;

import domain.services.HRManagerShiftService;
import domain.services.EmployeeShiftService;

public class ShiftController {
    private final HRManagerShiftService managerShiftService;
    private final EmployeeShiftService employeeShiftService;

    public ShiftController(HRManagerShiftService managerShiftService, EmployeeShiftService employeeShiftService) {
        this.managerShiftService = managerShiftService;
        this.employeeShiftService = employeeShiftService;
    }

    public String createShiftTemplate(String name) {
        return "Template '" + name + "' created successfully.";
    }

    public String setShiftTemplateAsDefault(String name) {
        return "Default template updated.";
    }

    public String placeToShifts() {
        return "Employees placed in shifts.";
    }

    public String issueReport() {
        return "HR Report Data will be displayed here...";
    }
}