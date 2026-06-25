package Employees.Service.Core;

import Core.Controller.ControllerFactory;
import Core.Service.Response;
import Employees.Domain.Entities.EmployeeDL;
import Employees.Domain.Entities.RequestDL;
import Employees.Domain.Entities.ShiftDL;
import Employees.Domain.Facades.EmployeesFacade;
import Employees.Shared.Enums.ShiftType;
import java.util.Map;

public class EmployeeService {
    private final EmployeesFacade facade;

    public EmployeeService(EmployeesFacade facade) {
        this.facade = facade;
    }

    public Response<EmployeeDL> getMyDetails(String id) {
        try {
            EmployeeDL emp = facade.getEmployee(id);
            if (emp == null) throw new RuntimeException("Employee not found.");
            return new Response<>(emp);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> updateEmployee(EmployeeDL emp, String password) {
        try {
            Response<Boolean> authRes = ControllerFactory.getInstance().getAuthController().verifyPassword(emp.getId(), password);
            if (!authRes.isSuccess() || !authRes.getData()) throw new RuntimeException("Authorization failed: Incorrect employee password.");
            facade.addUpdateEmployee(emp);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<String> submitRequest(RequestDL request) {
        try {
            facade.addUpdateRequest(request);
            return new Response<>("Success");
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /**
     * Reports additional hours for an employee.
     * Validates: shift exists, shift is DAY type, employee is assigned to it.
     */
    public Response<String> reportAdditionalHours(String employeeId, int shiftId, float hours) {
        try {
            if (hours <= 0) throw new RuntimeException("Hours must be a positive number.");
            ShiftDL shift = facade.getShift(shiftId);
            if (shift == null) throw new RuntimeException("Shift #" + shiftId + " not found.");
            if (shift.getShiftType() != ShiftType.DAY)
                throw new RuntimeException("Additional hours can only be reported for DAY shifts. Shift #" + shiftId + " is a " + shift.getShiftType().type + " shift.");
            if (!shift.doesEmployeeWork(employeeId))
                throw new RuntimeException("You are not assigned to shift #" + shiftId + ".");
            Map<String, Float> additionalHours = shift.getAdditionalHours();
            additionalHours.put(employeeId, hours);
            shift.setAdditionalHours(additionalHours);
            facade.addUpdateShift(shift);
            return new Response<>("Additional " + hours + " hours reported for shift #" + shiftId + ".");
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }
}