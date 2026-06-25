package Employees.Presentation.Controller;

import Employees.Service.Core.EmployeeService;
import Core.Service.Response;
import Employees.Domain.Entities.EmployeeDL;
import Employees.Domain.Entities.RequestDL;

@SuppressWarnings("ClassCanBeRecord")
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) { this.service = service; }

    public EmployeeDL getMyDetails(String id) {
        Response<EmployeeDL> res = service.getMyDetails(id);
        if (!res.isSuccess()) throw new RuntimeException(res.getErrorMessage());
        return res.getData();
    }

    public void submitRequest(RequestDL req) {
        Response<String> res = service.submitRequest(req);
        if (!res.isSuccess()) throw new RuntimeException(res.getErrorMessage());
    }
}