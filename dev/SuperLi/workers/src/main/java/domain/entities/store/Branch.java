package domain.entities.store;

import data_access.entities.BranchEntity;

public class Branch {
    public static final int ALL_BRANCHES = -1 ;
    private int branchId;
    private String managerId;
    private String location;

    public Branch(int branchId, String managerId, String location) {
        this.branchId = branchId;
        this.managerId = managerId;
        this.location = location;
    }

    public Branch(Branch branch) {
        this(branch.branchId, branch.managerId, branch.location);
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
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

    public BranchEntity toEntity(){
        return new BranchEntity(branchId, location);
    }

}
