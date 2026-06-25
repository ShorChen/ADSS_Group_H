package Employees.Presentation.Controller;

import Employees.Service.Core.HRService;
import Employees.Service.DTO.*;
import Employees.Presentation.DTO.*;
import Core.Service.Response;
import Employees.Domain.Entities.EmployeeDL;
import java.util.List;
import java.util.stream.Collectors;

public class HRController {
    private final HRService service;

    public HRController(HRService service) { this.service = service; }

    public void addUpdateEmployee(EmployeeDL emp) {
        Response<String> res = service.addUpdateEmployee(emp);
        if (!res.isSuccess()) throw new RuntimeException(res.getErrorMessage());
    }

    public List<EmployeePL> getAllEmployees() {
        Response<List<EmployeeSL>> res = service.getAllEmployees();
        if (!res.isSuccess()) throw new RuntimeException(res.getErrorMessage());
        return res.getData().stream()
                .map(sl -> new EmployeePL(sl.id(), sl.name(), sl.bankAccount(), sl.salary(), sl.salaryType(), sl.dateOfEmployment(), sl.jobScope(), sl.constraints(), sl.yearlyRestDays(), sl.weeklyRestDay(), sl.workingDoubles(), sl.active(), sl.branchId()))
                .collect(Collectors.toList());
    }
}