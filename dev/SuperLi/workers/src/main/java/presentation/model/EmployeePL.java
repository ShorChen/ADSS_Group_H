package presentation.model;

import domain.entities.Employee;
import domain.entities.Role;
import domain.enums.JobScope;
import domain.enums.SalaryType;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.*;

public class EmployeePL {
    private final String id;
    private String name;
    private String bankAccount;
    private double salary;
    private SalaryType salaryType;
    private LocalDateTime dateOfEmployment;
    private JobScope jobScope;
    private final List<String> qualifiedRoles;
    private String constraints;
    private int yearlyRestDays;
    private final WeekDay weeklyRestDay;
    private boolean workingDoubles;
    private Map<WeekDay, Set<ShiftType>> unavailableShifts;
    private boolean active = true;

    public EmployeePL(String id, String name, String bankAccount,
                      double salary, SalaryType salaryType, LocalDateTime dateOfEmployment,
                      JobScope jobScope, List<String> qualifiedRoles, String constraints,
                      int yearlyRestDays, WeekDay weeklyRestDay, boolean workingDoubles, Map<WeekDay, Set<ShiftType>> unavailableShifts) {
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
    }

    /**
     * @param employee the domain layer employee to be transformed.
     * @apiNote The constructor makes a deep copy of the fields of {@code employee}
     */
    public EmployeePL(Employee employee) {
        this(
                employee.getId(), employee.getName(), employee.getBankAccount(),
                employee.getSalary(), employee.getSalaryType(),
                employee.getDateOfEmployment(), employee.getJobScope(),
                new ArrayList<>(), employee.getConstraints(),
                employee.getYearlyRestDays(), employee.getWeeklyRestDay(), employee.isWorkingDoubles(),
                new HashMap<>()
        );

        List<String> roles = new ArrayList<>();
        employee.getQualifiedRoles().forEach(role -> roles.add(role.getTag()));
        addQualifiedRoles(roles.toArray(String[]::new));

        employee.getUnavailableShifts().forEach((weekDay,
                                                 shiftTypes) -> {
            Set<ShiftType> shiftTypesClone = new HashSet<>(shiftTypes);
            unavailableShifts.put(weekDay, shiftTypesClone);
        });
        this.active = employee.isActive();
    }

    /**
     * @return a domain layer employee transformed from {@code this}
     */
    public Employee toEmployee() {
        List<Role> roles = new ArrayList<>();
        qualifiedRoles.forEach(r -> roles.add(new Role(r)));
        return new Employee(
                id, name, bankAccount, salary, salaryType, dateOfEmployment,
                jobScope, roles, constraints, yearlyRestDays,
                weeklyRestDay, workingDoubles, getUnavailableShifts(), active
        );
    }

    /**
     * Adds roles to an employee. Each role is added only if the employee did not have that role already.
     *
     * @param roles the roles to add
     */
    public void addQualifiedRoles(String... roles) {
        for (String qualifiedRole : roles) {
            if (!this.qualifiedRoles.contains(qualifiedRole))
                this.qualifiedRoles.add(qualifiedRole);
        }
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

    /**
     * Retrieves a copied list of the roles of an employee.
     * The method does not give access to the internals of the object
     * you can add or remove roles using {@link #addQualifiedRoles(String...)} and {@link #removeQualifiedRoles(String...)}
     */
    public List<String> getQualifiedRoles() {
        return new ArrayList<>(qualifiedRoles);
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
        this.unavailableShifts = unavailableShifts;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static class Builder {
        private final String id;

        private String name;
        private String bankAccount;
        private double salary;
        private SalaryType salaryType;
        private LocalDateTime dateOfEmployment;
        private JobScope jobScope;
        private List<String> qualifiedRoles;
        private String constraints;
        private int yearlyRestDays;
        private WeekDay weeklyRestDay;
        private boolean workingDoubles;
        private Map<WeekDay, Set<ShiftType>> unavailableShifts;
        private boolean active = true;

        public Builder(String id) {
            this.id = id;
            this.qualifiedRoles = new ArrayList<>();
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

        public Builder qualifiedRoles(List<String> qualifiedRoles) {
            this.qualifiedRoles = qualifiedRoles != null ?
                    qualifiedRoles : new ArrayList<>();
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

        public EmployeePL build() {
            EmployeePL emp = new EmployeePL(id, name, bankAccount, salary, salaryType,
                    dateOfEmployment, jobScope, qualifiedRoles, constraints, yearlyRestDays,
                    weeklyRestDay, workingDoubles, unavailableShifts);
            emp.setActive(active);
            return emp;
        }

        public List<String> getQualifiedRoles() {
            return new ArrayList<>(qualifiedRoles);
        }
    }

}
