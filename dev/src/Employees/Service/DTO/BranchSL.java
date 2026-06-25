package Employees.Service.DTO;

public record BranchSL(
        int branchId,
        String location,
        String branchManagerId,
        int year,
        int week
) {}