package presentation.model;

import domain.entities.Employee;
import domain.entities.Role;
import domain.enums.JobScope;
import domain.enums.SalaryType;
import domain.enums.ShiftType;
import domain.enums.WeekDay;

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
    private final List<Role> qualifiedRoles;
    private String constraints;
    private int yearlyRestDays;
    private WeekDay weeklyRestDay;
    private boolean workingDoubles;
    private Map<WeekDay, Set<ShiftType>> unavailableShifts;
    private boolean active = true;

    public EmployeePL(String id, String name, String bankAccount,
                      double salary, SalaryType salaryType, LocalDateTime dateOfEmployment,
                      JobScope jobScope, List<Role> qualifiedRoles, String constraints,
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

    public EmployeePL(Employee employee) {
        this(
                employee.getId(), employee.getName(), employee.getBankAccount(),
                employee.getSalary(), employee.getSalaryType(),
                employee.getDateOfEmployment(), employee.getJobScope(),
                employee.getQualifiedRoles(), employee.getConstraints(),
                employee.getYearlyRestDays(), employee.getWeeklyRestDay(), employee.isWorkingDoubles(),
                employee.getUnavailableShifts()
        );
        this.active = employee.isActive();
    }

    public static class Builder {
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
        private boolean workingDoubles;
        private Map<WeekDay, Set<ShiftType>> unavailableShifts;
        private boolean active = true;

        public Builder(String id) {
            this.id = id;
            this.qualifiedRoles = new ArrayList<>();
            unavailableShifts = new HashMap<>();
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

        public Builder qualifiedRoles(List<Role> qualifiedRoles) {
            this.qualifiedRoles = qualifiedRoles != null ? qualifiedRoles : new ArrayList<>();
            return this;
        }

        public Builder addQualifiedRole(Role role) {
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
            emp.setActive(this.active);
            return emp;
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

    public Employee toEmployee() {
        return new Employee(
                id, name, bankAccount, salary, salaryType, dateOfEmployment,
                jobScope, new ArrayList<>(qualifiedRoles), constraints, yearlyRestDays,
                weeklyRestDay , workingDoubles, unavailableShifts, active
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
