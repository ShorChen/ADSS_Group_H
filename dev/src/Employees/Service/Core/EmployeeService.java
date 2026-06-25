package Employees.Service.Core;

import Core.Service.Response;
import Employees.Domain.Facades.EmployeesFacade;
import Employees.Domain.Entities.*;

public class EmployeeService {
    private final EmployeesFacade facade;

    public EmployeeService(EmployeesFacade facade) { this.facade = facade; }

    public Response<String> submitRequest(RequestDL request) {
        try {
            facade.addUpdateRequest(request);
            return new Response<>("Request submitted.");
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<EmployeeDL> getMyDetails(String myId) {
        try {
            EmployeeDL emp = facade.getEmployee(myId);
            if (emp == null) throw new IllegalArgumentException("Employee not found.");
            return new Response<>(emp);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }
}