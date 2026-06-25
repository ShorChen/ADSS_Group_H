package Employees.Domain.Entities;

import Employees.Shared.Enums.JobScope;
import Employees.Shared.Enums.SalaryType;
import Employees.Shared.Enums.WeekDay;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDL {
    private final String id;
    private String name;
    private String bankAccount;
    private double salary;
    private SalaryType salaryType;
    private LocalDateTime dateOfEmployment;
    private JobScope jobScope;
    private List<RoleDL> qualifiedRoles;
    private String constraints;
    private int yearlyRestDays;
    private WeekDay weeklyRestDay;
    private AvailabilitySubmissionDL availabilitySubmission;
    private boolean active;
    private int branchId;

    public EmployeeDL(String id, String name, String bankAccount,
                      double salary, SalaryType salaryType, LocalDateTime dateOfEmployment,
                      JobScope jobScope, List<RoleDL> qualifiedRoles, String constraints,
                      int yearlyRestDays, WeekDay weeklyRestDay,
                      AvailabilitySubmissionDL availabilitySubmission, boolean active,
                      int branchId) {
        this.id = id;
        this.name = name;
        this.bankAccount = bankAccount;
        this.salary = salary;
        this.salaryType = salaryType;
        this.dateOfEmployment = dateOfEmployment;
        this.jobScope = jobScope;
        setQualifiedRoles(qualifiedRoles);
        this.constraints = constraints;
        this.yearlyRestDays = yearlyRestDays;
        this.weeklyRestDay = weeklyRestDay;
        setAvailabilitySubmission(availabilitySubmission);
        this.active = active;
        this.branchId = branchId;
    }

    public List<RoleDL> getQualifiedRoles() {
        return new ArrayList<>(qualifiedRoles);
    }

    public void addQualifiedRoles(RoleDL... roles) {
        for (RoleDL role : roles) {
            if (!this.qualifiedRoles.contains(role)) this.qualifiedRoles.add(role);
        }
    }

    public void setQualifiedRoles(List<RoleDL> qualifiedRoles) {
        this.qualifiedRoles = new ArrayList<>();
        if (qualifiedRoles != null) this.qualifiedRoles.addAll(qualifiedRoles);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBankAccount() { return bankAccount; }
    public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public SalaryType getSalaryType() { return salaryType; }
    public void setSalaryType(SalaryType salaryType) { this.salaryType = salaryType; }
    public LocalDateTime getDateOfEmployment() { return dateOfEmployment; }
    public void setDateOfEmployment(LocalDateTime dateOfEmployment) { this.dateOfEmployment = dateOfEmployment; }
    public JobScope getJobScope() { return jobScope; }
    public void setJobScope(JobScope jobScope) { this.jobScope = jobScope; }
    public String getConstraints() { return constraints; }
    public void setConstraints(String constraints) { this.constraints = constraints; }
    public int getYearlyRestDays() { return yearlyRestDays; }
    public void setYearlyRestDays(int yearlyRestDays) { this.yearlyRestDays = yearlyRestDays; }
    public WeekDay getWeeklyRestDay() { return weeklyRestDay; }
    public void setWeeklyRestDay(WeekDay weeklyRestDay) { this.weeklyRestDay = weeklyRestDay; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public AvailabilitySubmissionDL getAvailabilitySubmission() {
        if (availabilitySubmission == null) return null;
        return new AvailabilitySubmissionDL(
                availabilitySubmission.getEmployeeId(),
                availabilitySubmission.getShifts(),
                availabilitySubmission.isWorkingDoubles()
        );
    }

    public void setAvailabilitySubmission(AvailabilitySubmissionDL availabilitySubmission) {
        if (availabilitySubmission == null) {
            this.availabilitySubmission = null;
            return;
        }
        this.availabilitySubmission = new AvailabilitySubmissionDL(
                availabilitySubmission.getEmployeeId(),
                availabilitySubmission.getShifts(),
                availabilitySubmission.isWorkingDoubles()
        );
    }
}