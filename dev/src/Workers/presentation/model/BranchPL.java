package Workers.presentation.model;

import Workers.data_access.entities.BranchEntity;
import Workers.domain.entities.store.Branch;

import java.util.ArrayList;
import java.util.List;

public class BranchPL {
    private String managerId;
    private String location;
    private List<String> employees;

    public BranchPL(String managerId, String location, List<String> employees) {
        this.managerId = managerId;
        this.location = location;
        this.employees = employees;
    }

    public BranchPL(BranchEntity branch) {
        this("", branch.location(), new ArrayList<>()); // todo remove employees
    }

    public Branch toBranch() {
        return new Branch(0, managerId, location);
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getEmployees() {
        return employees;
    }

    public void setEmployees(List<String> employees) {
        this.employees = employees;
    }
}
