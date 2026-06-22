package Workers.Domain.DTO;

import Workers.Shared.Enums.WeekDay;

import java.util.List;

@Deprecated
public class Store {
    private String name;
    private List<BranchSL> branches;
    private List<WeekDay> closedDays;
    private EmployeeSL manager;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BranchSL> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchSL> branches) {
        this.branches = branches;
    }
}
