package data_access.entities;

import java.time.LocalDateTime;
import java.util.*;

public class EmployeeEntity {
    private String id;
    private String name;
    private String bankAccount;
    private double salary;
    private String salaryType;
    private LocalDateTime dateOfEmployment;
    private String jobScope;
    private List<String> qualifiedRoles;
    private String constraints;
    private int yearlyRestDays;
    private String password;
    private boolean workingDoubles = false;
    private Map<Integer, Set<Integer>> unavailableShifts;

    public EmployeeEntity(String id, String password) {
        this(id, "temp-name", "0000-0000-0000-0000", 100.0, "HOURLY",
                LocalDateTime.now(),
                "FULL_TIME", new ArrayList<>(),
                "not free on weekends", 24, password, false, new HashMap<>()
        );

    }

    public EmployeeEntity(String id, String name, String bankAccount, double salary,
                          String salaryType, LocalDateTime dateOfEmployment,
                          String jobScope, List<String> qualifiedRoles,
                          String constraints, int yearlyRestDays,
                          String password, boolean workingDoubles,
                          Map<Integer, Set<Integer>> unavailableShifts) {
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
        this.password = password;
        this.workingDoubles = workingDoubles;
        this.unavailableShifts = unavailableShifts;
    }

    public String getId() {
        return id;
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

    public String getSalaryType() {
        return salaryType;
    }

    public LocalDateTime getDateOfEmployment() {
        return dateOfEmployment;
    }

    public String getJobScope() {
        return jobScope;
    }

    public List<String> getQualifiedRoles() {
        return qualifiedRoles;
    }

    public String getConstraints() {
        return constraints;
    }

    public int getYearlyRestDays() {
        return yearlyRestDays;
    }

    public boolean setPassword(String oldPass, String password) {
        if (this.password.equals(oldPass)) {
            this.password = password;
            return true;
        }
        return false;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isWorkingDoubles() {
        return workingDoubles;
    }

    public Map<Integer, Set<Integer>> getUnavailableShifts() {
        return unavailableShifts;
    }

    public void setQualifiedRoles(String... qualifiedRoles) {
        for (String qualifiedRole : qualifiedRoles) {
            if (!this.qualifiedRoles.contains(qualifiedRole))
                this.qualifiedRoles.add(qualifiedRole);
        }

    }

    public void update(EmployeeEntity entity) {
        if (!id.equals(entity.id))
            throw new IllegalArgumentException("Can not update an employee from another employee's account");
        if (!password.equals(entity.password))
            throw new IllegalArgumentException("Can not update without employee permission, password mismatch");

        name = entity.getName();
        bankAccount = entity.getBankAccount();
        salary = entity.getSalary();
        salaryType = entity.getSalaryType();
        dateOfEmployment = entity.getDateOfEmployment();
        jobScope = entity.getJobScope();
        qualifiedRoles = entity.getQualifiedRoles();
        constraints = entity.getConstraints();
        yearlyRestDays = entity.getYearlyRestDays();
        workingDoubles = entity.isWorkingDoubles();
        unavailableShifts = entity.getUnavailableShifts();
    }
}
