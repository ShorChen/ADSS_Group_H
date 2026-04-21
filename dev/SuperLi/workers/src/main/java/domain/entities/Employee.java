package domain.entities;

import data_access.entities.EmployeeEntity;
import domain.enums.JobScope;
import domain.enums.SalaryType;
import domain.enums.ShiftType;
import domain.enums.WeekDay;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

public class Employee {
    private final String id;
    private String name;
    private String bankAccount;
    private double salary;
    private SalaryType salaryType;
    private LocalDateTime dateOfEmployment;
    private JobScope jobScope;
    private final List<Role> qualifiedRoles;
    private String constraints;
    private int yearlyRestDays;
    private WeekDay weeklyRestDay;
    private boolean workingDoubles = false;
    private Map<WeekDay, Set<ShiftType>> unavailableShifts;
    private boolean active;

    public Employee(String id, String name, String bankAccount,
                    double salary, SalaryType salaryType, LocalDateTime dateOfEmployment,
                    JobScope jobScope, List<Role> qualifiedRoles, String constraints,
                    int yearlyRestDays, WeekDay weeklyRestDay, boolean workingDoubles,
                    Map<WeekDay, Set<ShiftType>> unavailableShifts, boolean active) {
        this.id = id;
        this.name = name;
        this.bankAccount = bankAccount;
        this.salary = salary;
        this.salaryType = salaryType;
        this.dateOfEmployment = dateOfEmployment;
        this.jobScope = jobScope;
        this.qualifiedRoles = qualifiedRoles;
        this.constraints = constraints;
        this.yearlyRestDays = yearlyRestDays;
        this.weeklyRestDay = weeklyRestDay;
        this.workingDoubles = workingDoubles;
        this.unavailableShifts = unavailableShifts;
        this.active = active;

    }

    public Employee(EmployeeEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.bankAccount = entity.getBankAccount();
        this.salary = entity.getSalary();
        this.salaryType = SalaryType.valueOf(entity.getSalaryType());
        this.dateOfEmployment = entity.getDateOfEmployment();
        this.jobScope = JobScope.valueOf(entity.getJobScope());
        this.qualifiedRoles = new ArrayList<>();
        this.qualifiedRoles.addAll(getQualifiedRoles());
        this.constraints = entity.getConstraints();
        this.weeklyRestDay = WeekDay.valueOf(entity.getWeeklyRestDay());
        this.yearlyRestDays = entity.getYearlyRestDays();
        this.workingDoubles = entity.isWorkingDoubles();
        this.active = entity.isActive();

        Map<Integer, Set<Integer>> entityUnavailableShifts = entity.getUnavailableShifts();
        this.unavailableShifts = new HashMap<>();

        entity.getQualifiedRoles().forEach(s -> qualifiedRoles.add(new Role(s)));

        if (entityUnavailableShifts != null) {
            entityUnavailableShifts.forEach((day, shifts) -> {
                HashSet<ShiftType> shiftTypes = new HashSet<>();
                shifts.forEach(i -> shiftTypes.add(ShiftType.values()[i]));
                this.unavailableShifts.put(WeekDay.values()[day], shiftTypes);
            });
        }
    }


    public String getId() {
        return id;
    }

    public List<Role> getQualifiedRoles() {
        return qualifiedRoles;
    }

    public void setQualifiedRoles(Role... qualifiedRoles) {
        for (Role qualifiedRole : qualifiedRoles) {
            if (!this.qualifiedRoles.contains(qualifiedRole))
                this.qualifiedRoles.add(qualifiedRole);
        }

    }

    public EmployeeEntity toEntity(String password) {
        List<String> roles = new ArrayList<>();
        qualifiedRoles.forEach(role -> roles.add(role.getTag()));
        Map<Integer, Set<Integer>> unavailableShiftsEntity = new HashMap<>();

        unavailableShifts.forEach((day, shiftTypes) -> {
            Set<Integer> shifts = new HashSet<>();
            shiftTypes.forEach(type -> shifts.add(type.ordinal()));
            unavailableShiftsEntity.put(day.ordinal(), shifts);
        });

        return new EmployeeEntity(
                id, name, bankAccount, salary, salaryType.name(), dateOfEmployment,
                jobScope.name(), roles, constraints, yearlyRestDays, weeklyRestDay.name(),
                password, workingDoubles,
                unavailableShiftsEntity, active
        );
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setSalaryType(SalaryType salaryType) {
        this.salaryType = salaryType;
    }

    public void setDateOfEmployment(LocalDateTime dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }

    public void setJobScope(JobScope jobScope) {
        this.jobScope = jobScope;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    public void setYearlyRestDays(int yearlyRestDays) {
        this.yearlyRestDays = yearlyRestDays;
    }

    public void setWorkingDoubles(boolean workingDoubles) {
        this.workingDoubles = workingDoubles;
    }

    public void setUnavailableShifts(Map<WeekDay, Set<ShiftType>> unavailableShifts) {
        this.unavailableShifts = unavailableShifts;
    }

    public String getName() {
        return name;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public double getSalary() {
        return salary;
    }

    public SalaryType getSalaryType() {
        return salaryType;
    }

    public LocalDateTime getDateOfEmployment() {
        return dateOfEmployment;
    }

    public JobScope getJobScope() {
        return jobScope;
    }

    public String getConstraints() {
        return constraints;
    }

    public int getYearlyRestDays() {
        return yearlyRestDays;
    }

    public WeekDay getWeeklyRestDay() {
        return weeklyRestDay;
    }

    public boolean isWorkingDoubles() {
        return workingDoubles;
    }

    public Map<WeekDay, Set<ShiftType>> getUnavailableShifts() {
        return unavailableShifts;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}