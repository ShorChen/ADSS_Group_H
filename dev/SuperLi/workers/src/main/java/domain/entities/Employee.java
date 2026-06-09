package domain.entities;

import data_access.entities.EmployeeEntity;
import org.jetbrains.annotations.NotNull;
import shared.enums.JobScope;
import shared.enums.SalaryType;
import shared.enums.ShiftType;
import shared.enums.WeekDay;

import java.time.LocalDateTime;
import java.util.*;

public class Employee {
    private final String id;
    private String name;
    private String bankAccount;
    private double salary;
    private SalaryType salaryType;
    private LocalDateTime dateOfEmployment;
    private JobScope jobScope;
    private List<Role> qualifiedRoles;
    private String constraints;
    private int yearlyRestDays;
    private WeekDay weeklyRestDay;
    private boolean workingDoubles = false;
    private Map<WeekDay, Set<ShiftType>> unavailableShifts;
    private boolean active;
    private int branchId;

    public Employee(String id, String name, String bankAccount,
                    double salary, SalaryType salaryType, LocalDateTime dateOfEmployment,
                    JobScope jobScope, List<Role> qualifiedRoles, String constraints,
                    int yearlyRestDays, WeekDay weeklyRestDay, boolean workingDoubles,
                    Map<WeekDay, Set<ShiftType>> unavailableShifts, boolean active,
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
        this.workingDoubles = workingDoubles;
        setUnavailableShifts(unavailableShifts);
        this.active = active;
        this.branchId = branchId;

    }

    public Employee(@NotNull EmployeeEntity e) {
        this(e.id(), e.name(), e.bankAccount(), e.salary(),
                SalaryType.fromArgs(e.salaryType()), e.dateOfEmployment(),
                JobScope.fromArgs(e.jobScope()),
                new ArrayList<>(), e.constraints(), e.yearlyRestDays(),
                WeekDay.fromArgs(e.weeklyRestDay()),
                e.workingDoubles(),
                new HashMap<>(),
                e.active(), e.branchId()
        );

        qualifiedRoles = new ArrayList<>();
        e.qualifiedRoles().forEach(role -> qualifiedRoles.add(new Role(role)));

        unavailableShifts = new HashMap<>();
        e.unavailableShifts().forEach((key, value) -> {
            Set<ShiftType> shiftTypes = new HashSet<>();
            value.forEach(i -> shiftTypes.add(ShiftType.fromInteger(i)));
            unavailableShifts.put(WeekDay.fromInteger(key), shiftTypes);
        });
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
                unavailableShiftsEntity, active, branchId
        );
    }

    public List<Role> getQualifiedRoles() {
        return new ArrayList<>(qualifiedRoles);
    }

    public void addQualifiedRoles(Role... qualifiedRoles) {
        for (Role qualifiedRole : qualifiedRoles) {
            if (!this.qualifiedRoles.contains(qualifiedRole))
                this.qualifiedRoles.add(qualifiedRole);
        }

    }

    public void setQualifiedRoles(List<Role> qualifiedRoles) {
        this.qualifiedRoles = new ArrayList<>();
        this.qualifiedRoles.addAll(qualifiedRoles);
    }

    public void setUnavailableShifts(Map<WeekDay, Set<ShiftType>> unavailableShifts) {
        this.unavailableShifts = new HashMap<>();
        unavailableShifts.forEach((weekDay, shiftTypes) ->
                this.unavailableShifts.put(weekDay, new HashSet<>(shiftTypes)));
    }

    public Map<WeekDay, Set<ShiftType>> getUnavailableShifts() {
        Map<WeekDay, Set<ShiftType>> result = new HashMap<>();
        unavailableShifts.forEach((weekDay, shiftTypes) -> {
            result.put(weekDay, new HashSet<>(shiftTypes));
        });
        return result;
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

    public boolean isWorkingDoubles() {
        return workingDoubles;
    }

    public void setWorkingDoubles(boolean workingDoubles) {
        this.workingDoubles = workingDoubles;
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
}