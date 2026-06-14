package Workers.presentation.model;

import Workers.domain.entities.store.Branch;
import Workers.shared.enums.WeekDay;

import java.util.ArrayList;
import java.util.List;

public class StoreDetailsPL {
    private List<Branch> branches;
    private String manager;
    private List<WeekDay> closedDays;

    public StoreDetailsPL(List<Branch> branches, String manager, List<WeekDay> closedDays) {
        setBranches(branches);
        this.manager = manager;
        setClosedDays(closedDays);
    }

    public List<Branch> getBranches() {
        return new ArrayList<>(branches);
    }

    public void setBranches(List<Branch> branches) {
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
