package Employees.DataAccess.Entities;

import Employees.Context.SessionManager;

import java.time.LocalDateTime;
import java.util.*;

public record EmployeeEntity(
        String id,
        String name,
        String bankAccount,
        double salary,
        String salaryType,
        LocalDateTime dateOfEmployment,
        String jobScope,
        List<String> qualifiedRoles,
        String constraints,
        int yearlyRestDays,
        String weeklyRestDay,
        String password,
        AvailabilitySubmissionEntity availabilitySubmission,
        boolean active,
        int branchId
) {
    public EmployeeEntity(String id, String password) {
        this(id, "temp-name", "0000-0000-0000-0000", 100.0,
                "HOURLY",
                SessionManager.now(),
                "FULL_TIME", new ArrayList<>(),
                "not free on weekends", 24,
                "SATURDAY", password,
                new AvailabilitySubmissionEntity(id, new HashMap<>(),
                        false),
                true, 1
        );
    }

    public EmployeeEntity(String id, String name, String bankAccount, double salary,
                          String salaryType, LocalDateTime dateOfEmployment,
                          String jobScope, List<String> qualifiedRoles,
                          String constraints, int yearlyRestDays,
                          String weeklyRestDay, String password, AvailabilitySubmissionEntity availabilitySubmission,
                          boolean active, int branchId) {
        this.id = id;
        this.name = name;
        this.bankAccount = bankAccount;
        this.salary = salary;
        this.salaryType = salaryType;
        this.dateOfEmployment = dateOfEmployment;
        this.jobScope = jobScope;

        this.qualifiedRoles = new ArrayList<>();
        this.qualifiedRoles.addAll(qualifiedRoles);

        this.constraints = constraints;
        this.yearlyRestDays = yearlyRestDays;
        this.weeklyRestDay = weeklyRestDay;
        this.password = password;

        this.availabilitySubmission = availabilitySubmission;

        this.active = active;
        this.branchId = branchId;
    }

    public EmployeeEntity changePassword(String password) {
        return new EmployeeEntity(id, name, bankAccount, salary, salaryType,
                dateOfEmployment, jobScope, qualifiedRoles,
                constraints, yearlyRestDays, weeklyRestDay, password,
                availabilitySubmission,
                active, branchId);
    }

    public EmployeeEntity changeActivityStatus(boolean active) {
        return new EmployeeEntity(id, name, bankAccount, salary, salaryType,
                dateOfEmployment, jobScope, qualifiedRoles,
                constraints, yearlyRestDays, weeklyRestDay, password,
                availabilitySubmission,
                active, branchId);
    }

    public EmployeeEntity changeAvailability(AvailabilitySubmissionEntity availabilitySubmission) {
        return new EmployeeEntity(id, name, bankAccount, salary, salaryType,
                dateOfEmployment, jobScope, qualifiedRoles,
                constraints, yearlyRestDays, weeklyRestDay, password,
                availabilitySubmission,
                active, branchId);
    }

    public EmployeeEntity changeQualifiedRoles(List<String> qualifiedRoles) {
        return new EmployeeEntity(id, name, bankAccount, salary, salaryType,
                dateOfEmployment, jobScope, qualifiedRoles,
                constraints, yearlyRestDays, weeklyRestDay, password,
                availabilitySubmission,
                active, branchId);
    }

    public List<String> qualifiedRoles() {
        return new ArrayList<>(qualifiedRoles);
    }

    @Override
    public AvailabilitySubmissionEntity availabilitySubmission() {
        return new AvailabilitySubmissionEntity(
                id,
                availabilitySubmission.shifts(),
                availabilitySubmission.workingDoubles()
        );
    }

    public boolean checkPassword(String password) {
        return Objects.equals(password, this.password);
    }

}
