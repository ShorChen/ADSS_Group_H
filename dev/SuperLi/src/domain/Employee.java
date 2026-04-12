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
}
