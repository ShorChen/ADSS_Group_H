package domain.entities;

import domain.enums.JobScope;
import domain.enums.SalaryType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee {
    private String id;
    private String name;
    private String bankAccount;
    private double salary;
    private SalaryType salaryType;
    private Date dateOfEmployment;
    private JobScope jobScope;
    private List<Role> qualifiedRoles;
    private String constraints;
    private int yearlyRestDays;

    public Employee(String id) {
        this.id = id;
        qualifiedRoles = new ArrayList<>();
    }

    public Employee(String id, String name, String bankAccount,
                    double salary, SalaryType salaryType, Date dateOfEmployment,
                    JobScope jobScope, List<Role> qualifiedRoles, String constraints,
                    int yearlyRestDays) {
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
    }

    public String getId() {
        return id;
    }

    public List<Role> getQualifiedRoles() {
        return qualifiedRoles;
    }

    public void setQualifiedRoles(Role... qualifiedRoles) {
        if (this.qualifiedRoles == null) {
            this.qualifiedRoles = new ArrayList<>();
        }
        for (Role qualifiedRole : qualifiedRoles) {
            if (!this.qualifiedRoles.contains(qualifiedRole))
                this.qualifiedRoles.add(qualifiedRole);
        }

    }
}
