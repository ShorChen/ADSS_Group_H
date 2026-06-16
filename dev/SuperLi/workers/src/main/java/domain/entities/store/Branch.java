package domain.entities.store;

import data_access.entities.BranchEntity;

public class Branch {
    public static final int ALL_BRANCHES = -1 ;
    private int branchId;
    private String location;

    public Branch(int branchId, String location) {
        this.branchId = branchId;
        this.location = location;
    }

    public Branch(BranchEntity branch) {
        this(branch.branchId(), branch.location());
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BranchEntity toEntity(){
        return new BranchEntity(branchId, location);
    }

}
