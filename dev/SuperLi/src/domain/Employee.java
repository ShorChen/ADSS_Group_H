package domain;
import java.util.ArrayList;
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

    public Employee(String id, String name, String bankAccount, double salary, String employmentTerms, Date startDate) {
        this.id = id;
        this.name = name;
        this.bankAccount = bankAccount;
        this.salary = salary;
        this.employmentTerms = employmentTerms;
        this.startDate = startDate;
        this.qualifiedRoles = new ArrayList<>();
        this.constraints = new ArrayList<>();
    }

    public boolean isHRManager(){
        for(Role role : qualifiedRoles){
            if(role.getName().equals("HR Manager")){
                return true;
            }
        }
        return false;
    }

    public boolean addQualifiedRole(Role role) {    
        if (isHRManager() == false) {
            throw new IllegalStateException("Only HR Managers can add qualified roles to employees.");
        }
        if (!qualifiedRoles.contains(role)) {
            qualifiedRoles.add(role);
            return true;
        }
        return false;
    }

    public List<Constraint> getConstraints(){
        return constraints;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    



    
}

