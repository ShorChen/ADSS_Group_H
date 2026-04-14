package domain;
import java.util.Date;
import java.util.List;

public class Employee {
    private String id;
    private String name;
    private String bankAccount;
    private double salary;
    private String employmentTerms;
    private Date startDate;

    private List<Role> qualifiedRoles;
    private List<Constraint> constraints;

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

    public String getEmploymentTerms() {
        return employmentTerms;
    }

    public Date getStartDate() {
        return startDate;
    }

    public List<Role> getQualifiedRoles() {
        return qualifiedRoles;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }
}
