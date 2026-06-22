package Workers.Presentation.DTO;

import Workers.Domain.DTO.BranchSL;
import Workers.Shared.Enums.WeekDay;

import java.util.ArrayList;
import java.util.List;

public class StoreDetailsPL {
    private List<BranchSL> branches;
    private String manager;
    private List<WeekDay> closedDays;

    public StoreDetailsPL(List<BranchSL> branches, String manager, List<WeekDay> closedDays) {
        setBranches(branches);
        this.manager = manager;
        setClosedDays(closedDays);
    }

    public List<BranchSL> getBranches() {
        return new ArrayList<>(branches);
    }

    public void setBranches(List<BranchSL> branches) {
        this.branches = new ArrayList<>(branches);
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public List<WeekDay> getClosedDays() {
        return new ArrayList<>(closedDays);
    }

    public void setClosedDays(List<WeekDay> closedDays) {
        this.closedDays = new ArrayList<>(closedDays);
    }
}
