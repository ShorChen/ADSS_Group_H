package domain.entities;

import domain.enums.JobScope;
import domain.enums.SalaryType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee {
    private String id;
    private String name;
    private boolean isActive;
    private String bankAccount;
    private double salary;
    private SalaryType salaryType;
    private Date dateOfEmployment;
    private JobScope jobScope;
    private List<Role> qualifiedRoles;
    private String constraints;
    private int yearlyRestDays;

    public Employee(String id)
    {
        this.id = id;
        qualifiedRoles = new ArrayList<>();
    }
    public String getId() {
        return id;
    }

    public List<Role> getQualifiedRoles() {
        return qualifiedRoles;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean setActive(boolean active) {
        this.isActive = active;
        return isActive;
    }

    public void setQualifiedRoles(Role... qualifiedRoles) {
        if (this.qualifiedRoles == null){
            this.qualifiedRoles = new ArrayList<>();
        }
        for (Role qualifiedRole : qualifiedRoles) {
            if (!this.qualifiedRoles.contains(qualifiedRole))
                this.qualifiedRoles.add(qualifiedRole);
        }

    }

    public void addRole(Role role) {
        if (!qualifiedRoles.contains(role)) {
            qualifiedRoles.add(role);
        }
    }
}
