package Employees.Service.Core;

import Core.Service.Response;
import Employees.Domain.Facades.EmployeesFacade;
import Employees.Domain.Entities.*;
import Employees.Service.DTO.*;
import java.util.List;
import java.util.stream.Collectors;

public class HRService {
    private final EmployeesFacade facade;

    public HRService(EmployeesFacade facade) { this.facade = facade; }

    public Response<String> addUpdateEmployee(EmployeeDL employeeDL) {
        try {
            facade.addUpdateEmployee(employeeDL);
            return new Response<>("Employee saved successfully.");
        } catch (Exception e) {
            return new Response<>("Error saving employee: " + e.getMessage());
        }
    }

    public Response<List<EmployeeSL>> getAllEmployees() {
        try {
            List<EmployeeSL> list = facade.getAllEmployees().stream()
                    .map(e -> new EmployeeSL(e.getId(), e.getName(), e.getBankAccount(), e.getSalary(), e.getSalaryType().name(), e.getDateOfEmployment(), e.getJobScope().name(), e.getConstraints(), e.getYearlyRestDays(), e.getWeeklyRestDay().name(), e.getAvailabilitySubmission() != null && e.getAvailabilitySubmission().isWorkingDoubles(), e.isActive(), e.getBranchId()))
                    .collect(Collectors.toList());
            return new Response<>(list);
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<StoreDetailsSL> getStoreDetails() {
        try {
            StoreDetailsDL dl = facade.getStoreDetails();
            return new Response<>(new StoreDetailsSL(dl.isFirstStartUp(), dl.getClosedDays()));
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }

    public Response<String> updateStoreDetails(StoreDetailsSL sl) {
        try {
            facade.updateStoreDetails(new StoreDetailsDL(sl.firstStartUp(), sl.closedDays()));
            return new Response<>("Store details updated.");
        } catch (Exception e) { return new Response<>(e.getMessage()); }
    }
}