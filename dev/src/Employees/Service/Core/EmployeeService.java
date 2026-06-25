package Employees.Service.Core;

import Core.Controller.ControllerFactory;
import Core.Service.Response;
import Employees.Domain.Entities.EmployeeDL;
import Employees.Domain.Entities.RequestDL;
import Employees.Domain.Facades.EmployeesFacade;

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
}