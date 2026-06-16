package presentation.model;

import domain.entities.store.Branch;

public class BranchPL {
    private String location;

    public BranchPL(String location) {
        this.location = location;
    }

    public BranchPL(Branch branch) {
        this(branch.getLocation());
    }

    public Branch toBranch() {
        return new Branch(0, location);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
