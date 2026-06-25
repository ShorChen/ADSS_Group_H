package Employees.Presentation.DTO;
import java.time.LocalDateTime;

public record EmployeePL(String id, String name, String bankAccount, double salary, String salaryType, LocalDateTime dateOfEmployment, String jobScope, String constraints, int yearlyRestDays, String weeklyRestDay, boolean workingDoubles, boolean active, int branchId) {}