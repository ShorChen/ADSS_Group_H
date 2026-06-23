package Employees.Presentation.DTO;

import Employees.Domain.Entities.EmployeeSL;
import Employees.Domain.Entities.RoleSL;
import Employees.Shared.Enums.JobScope;
import Employees.Shared.Enums.SalaryType;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.*;

public class EmployeePL {
    private String id;
    private String name;
    private String bankAccount;
    private double salary;
    private SalaryType salaryType;
    private LocalDateTime dateOfEmployment;
    private JobScope jobScope;
    private Set<String> qualifiedRoles;
    private String constraints;
    private int yearlyRestDays;
    private WeekDay weeklyRestDay;
    private boolean workingDoubles;
    private Map<WeekDay, Set<ShiftType>> unavailableShifts;
    private boolean active = true;
    private int branchId;

    public EmployeePL(String id, String name, String bankAccount,
                      double salary, SalaryType salaryType, LocalDateTime dateOfEmployment,
                      JobScope jobScope, Set<String> qualifiedRoles, String constraints,
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

    /**
     * @param employee the domain layer employee to be transformed.
     * @apiNote The constructor makes a deep copy of the fields of {@code employee}
     */
    public EmployeePL(EmployeeSL employee) {
        this(
                employee.getId(), employee.getName(), employee.getBankAccount(),
                employee.getSalary(), employee.getSalaryType(),
                employee.getDateOfEmployment(), employee.getJobScope(),
                new HashSet<>(), employee.getConstraints(),
                employee.getYearlyRestDays(), employee.getWeeklyRestDay(), employee.isWorkingDoubles(),
                new HashMap<>(), employee.isActive(), employee.getBranchId()
        );
        employee.getQualifiedRoles().forEach(role ->
                qualifiedRoles.add(role.getTag()));

        employee.getQualifiedRoles().forEach(r ->
                this.qualifiedRoles.add(r.getTag())
        );

        employee.getUnavailableShifts().forEach((weekDay, shiftTypes) ->
                unavailableShifts.put(weekDay, new HashSet<>(shiftTypes)));
    }

    /**
     * @return a domain layer employee transformed from {@code this}
     */
    public EmployeeSL toEmployee() {
        List<RoleSL> roles = new ArrayList<>();
        qualifiedRoles.forEach(r -> roles.add(new RoleSL(r)));
        return new EmployeeSL(
                id, name, bankAccount, salary, salaryType, dateOfEmployment,
                jobScope, roles, constraints, yearlyRestDays,
                weeklyRestDay, workingDoubles, getUnavailableShifts(), active, branchId
        );
    }

    public boolean containsRole(String role) {
        return qualifiedRoles.contains(role);
    }

    /**
     * Adds roles to an employee. Each role is added only if the employee did not have that role already.
     *
     * @param roles the roles to add
     */
    public void addQualifiedRoles(String... roles) {
        this.qualifiedRoles.addAll(Arrays.asList(roles));
    }

    /**
     * Removes the roles specified in {@code roles}
     *
     * @param roles the roles to remove
     */
    public void removeQualifiedRoles(String... roles) {
        for (String role : roles)
            qualifiedRoles.remove(role);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    /**
     * Retrieves a copied list of the roles of an employee.
     * The method does not give access to the internals of the object
     * you can add or remove roles using {@link #addQualifiedRoles(String...)} and {@link #removeQualifiedRoles(String...)}
     */
    public Set<String> getQualifiedRoles() {
        return new HashSet<>(qualifiedRoles);
    }

    public void setQualifiedRoles(Set<String> qualifiedRoles) {
        this.qualifiedRoles = new HashSet<>(qualifiedRoles);
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

    /**
     * @return a deep copy of the map of unavailable shifts of an employee
     */
    public Map<WeekDay, Set<ShiftType>> getUnavailableShifts() {
        Map<WeekDay, Set<ShiftType>> map = new HashMap<>();
        unavailableShifts.forEach((weekDay,
                                   shiftTypes) -> {
            Set<ShiftType> shiftTypesClone = new HashSet<>(shiftTypes);
            map.put(weekDay, shiftTypesClone);
        });
        return map;
    }

    public void setUnavailableShifts(Map<WeekDay, Set<ShiftType>> unavailableShifts) {
        this.unavailableShifts = new HashMap<>();
        unavailableShifts.forEach((key, value) ->
            this.unavailableShifts.put(key, new HashSet<>(value))
        );
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

    public static class Builder {
        private final String id;
        private String name;
        private String bankAccount;
        private double salary;
        private SalaryType salaryType;
        private LocalDateTime dateOfEmployment;
        private JobScope jobScope;
        private Set<String> qualifiedRoles;
        private String constraints;
        private int yearlyRestDays;
        private WeekDay weeklyRestDay;
        private boolean workingDoubles;
        private Map<WeekDay, Set<ShiftType>> unavailableShifts;
        private boolean active = true;
        private int branchId;

        public Builder(String id) {
            this.id = id;
            this.qualifiedRoles = new HashSet<>();
            this.unavailableShifts = new HashMap<>();
        }

        public Builder(@NotNull EmployeePL employee) {
            this.id = employee.getId();
            this.name = employee.getName();
            this.bankAccount = employee.getBankAccount();
            this.salary = employee.getSalary();
            this.salaryType = employee.getSalaryType();
            this.dateOfEmployment = employee.getDateOfEmployment();
            this.jobScope = employee.getJobScope();
            this.qualifiedRoles = employee.getQualifiedRoles();
            this.constraints = employee.getConstraints();
            this.yearlyRestDays = employee.getYearlyRestDays();
            this.weeklyRestDay = employee.getWeeklyRestDay();
            this.workingDoubles = employee.isWorkingDoubles();
            this.unavailableShifts = employee.getUnavailableShifts();
            this.branchId = employee.getBranchId();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder bankAccount(String bankAccount) {
            this.bankAccount = bankAccount;
            return this;
        }

        public Builder salary(double salary) {
            this.salary = salary;
            return this;
        }

        public Builder salaryType(SalaryType salaryType) {
            this.salaryType = salaryType;
            return this;
        }

        public Builder dateOfEmployment(LocalDateTime dateOfEmployment) {
            this.dateOfEmployment = dateOfEmployment;
            return this;
        }

        public Builder jobScope(JobScope jobScope) {
            this.jobScope = jobScope;
            return this;
        }

        public Builder qualifiedRoles(Set<String> qualifiedRoles) {
            this.qualifiedRoles = qualifiedRoles != null ?
                    qualifiedRoles : new HashSet<>();
            return this;
        }

        public Builder addQualifiedRole(String role) {
            if (!this.qualifiedRoles.contains(role)) {
                this.qualifiedRoles.add(role);
            }
            return this;
        }

        public Builder constraints(String constraints) {
            this.constraints = constraints;
            return this;
        }

        public Builder yearlyRestDays(int yearlyRestDays) {
            this.yearlyRestDays = yearlyRestDays;
            return this;
        }

        public Builder weeklyRestDay(WeekDay weeklyRestDay) {
            this.weeklyRestDay = weeklyRestDay;
            return this;
        }

        public Builder workingDoubles(boolean workingDoubles) {
            this.workingDoubles = workingDoubles;
            return this;
        }

        public Builder unavailableShifts(Map<WeekDay, Set<ShiftType>> unavailableShifts) {
            this.unavailableShifts = unavailableShifts;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder branchId(int branchId){
            this.branchId = branchId;
            return this;
        }

        public EmployeePL build() {
            return new EmployeePL(id, name, bankAccount, salary, salaryType,
                    dateOfEmployment, jobScope, qualifiedRoles, constraints, yearlyRestDays,
                    weeklyRestDay, workingDoubles, unavailableShifts, active, branchId);
        }

        public HashSet<String> getQualifiedRoles() {
            return new HashSet<>(qualifiedRoles);
        }
    }

}
