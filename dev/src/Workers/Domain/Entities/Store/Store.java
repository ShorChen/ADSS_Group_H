package Workers.Domain.Entities.Store;

import Workers.Domain.Entities.Employee;
import Workers.Shared.Enums.WeekDay;

import java.util.List;

public class Store {
    // todo implement

    private String name;
    private List<Branch> branches;
    private List<WeekDay> closedDays;
    private Employee manager;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }
}
