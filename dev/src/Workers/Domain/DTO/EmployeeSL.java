package Workers.Domain.DTO;

import Workers.DataAccess.Entities.EmployeeEntity;
import Workers.Shared.Enums.JobScope;
import Workers.Shared.Enums.SalaryType;
import Workers.Shared.Enums.WeekDay;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSL {
    private final String id;
    private String name;
    private String bankAccount;
    private double salary;
    private SalaryType salaryType;
    private LocalDateTime dateOfEmployment;
    private JobScope jobScope;
    private List<RoleSL> qualifiedRoles;
    private String constraints;
    private int yearlyRestDays;
    private WeekDay weeklyRestDay;
    private AvailabilitySubmissionSL availabilitySubmission;
    private boolean active;
    private int branchId;

    public EmployeeSL(String id, String name, String bankAccount,
                      double salary, SalaryType salaryType, LocalDateTime dateOfEmployment,
                      JobScope jobScope, List<RoleSL> qualifiedRoles, String constraints,
                      int yearlyRestDays, WeekDay weeklyRestDay,
                      AvailabilitySubmissionSL availabilitySubmission, boolean active,
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

    public EmployeeSL(@NotNull EmployeeEntity e) {
        this(e.id(), e.name(), e.bankAccount(), e.salary(),
                SalaryType.fromArgs(e.salaryType()), e.dateOfEmployment(),
                JobScope.fromArgs(e.jobScope()),
                new ArrayList<>(), e.constraints(), e.yearlyRestDays(),
                WeekDay.fromArgs(e.weeklyRestDay()),
                new AvailabilitySubmissionSL(e.availabilitySubmission()),
                e.active(), e.branchId()
        );

        qualifiedRoles = new ArrayList<>();
        e.qualifiedRoles().forEach(role -> qualifiedRoles.add(new RoleSL(role)));
    }

    public EmployeeEntity toEntity(String password) {
        List<String> roles = new ArrayList<>();
        qualifiedRoles.forEach(role -> roles.add(role.getTag()));

        return new EmployeeEntity(
                id, name, bankAccount, salary, salaryType.name(), dateOfEmployment,
                jobScope.name(), roles, constraints, yearlyRestDays, weeklyRestDay.name(),
                password, availabilitySubmission.toEntity(), active, branchId
        );
    }

    public List<RoleSL> getQualifiedRoles() {
        return new ArrayList<>(qualifiedRoles);
    }

    public void addQualifiedRoles(RoleSL... qualifiedRoles) {
        for (RoleSL qualifiedRole : qualifiedRoles) {
            if (!this.qualifiedRoles.contains(qualifiedRole))
                this.qualifiedRoles.add(qualifiedRole);
        }

    }

    public void setQualifiedRoles(List<RoleSL> qualifiedRoles) {
        this.qualifiedRoles = new ArrayList<>();
        this.qualifiedRoles.addAll(qualifiedRoles);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public SalaryType getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(SalaryType salaryType) {
        this.salaryType = salaryType;
    }

    public LocalDateTime getDateOfEmployment() {
        return dateOfEmployment;
    }

    public void setDateOfEmployment(LocalDateTime dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }

    public JobScope getJobScope() {
        return jobScope;
    }

    public void setJobScope(JobScope jobScope) {
        this.jobScope = jobScope;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    public int getYearlyRestDays() {
        return yearlyRestDays;
    }

    public void setYearlyRestDays(int yearlyRestDays) {
        this.yearlyRestDays = yearlyRestDays;
    }

    public WeekDay getWeeklyRestDay() {
        return weeklyRestDay;
    }

    public void setWeeklyRestDay(WeekDay weeklyRestDay) {
        this.weeklyRestDay = weeklyRestDay;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public AvailabilitySubmissionSL getAvailabilitySubmission() {
        return new AvailabilitySubmissionSL(
                availabilitySubmission.getEmployeeId(),
                availabilitySubmission.getShifts(),
                availabilitySubmission.isWorkingDoubles()
        );
    }

    public void setAvailabilitySubmission(AvailabilitySubmissionSL availabilitySubmission) {
        this.availabilitySubmission = new AvailabilitySubmissionSL(
                availabilitySubmission.getEmployeeId(),
                availabilitySubmission.getShifts(),
                availabilitySubmission.isWorkingDoubles()
        );
    }
}