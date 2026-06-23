package Employees.Domain.DTO;

import Employees.DataAccess.Entities.BranchEntity;
import Employees.DataAccess.Entities.Keys.WeekKey;

public class BranchSL {
    public static final int ALL_BRANCHES = -1 ;
    private int branchId;
    private String location;
    private String branchManagerId;
    private int startYear;
    private int startWeek;

    public BranchSL(int branchId, String location, String branchManagerId, int startYear, int startWeek) {
        this.branchId = branchId;
        this.location = location;
        this.branchManagerId = branchManagerId;
        this.startYear = startYear;
        this.startWeek = startWeek;
    }

    public BranchSL(BranchSL branch) {
        this(branch.branchId, branch.location, branch.branchManagerId, branch.startYear, branch.startWeek);
    }

    public BranchSL(BranchEntity branch) {
        this(branch.branchId(), branch.location(), branch.branchManagerId(),
                branch.startDate().year(), branch.startDate().week());
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

    public String getBranchManagerId() {
        return branchManagerId;
    }

    public void setBranchManagerId(String branchManagerId) {
        this.branchManagerId = branchManagerId;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public BranchEntity toEntity(){
        return new BranchEntity(branchId, location, branchManagerId,
                new WeekKey(startYear, startWeek));
    }

}
