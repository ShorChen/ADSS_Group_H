package Employees.Presentation.DTO;

import Employees.Domain.DTO.BranchSL;

public class BranchPL {
    private int branchId;
    private String location;
    private String branchManagerId;
    private int startYear;
    private int startWeek;

    public BranchPL(int branchId, String location, String branchManagerId, int startYear, int startWeek) {
        this.branchId = branchId;
        this.location = location;
        this.branchManagerId = branchManagerId;
        this.startYear = startYear;
        this.startWeek = startWeek;
    }

    public BranchPL(BranchSL b) {
        this(b.getBranchId(), b.getLocation(), b.getBranchManagerId(),
                b.getStartYear(), b.getStartWeek());
        branchId = b.getBranchId();
    }

    public BranchSL toBranch() {
        return new BranchSL(0, location, branchManagerId, startYear, startWeek);
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
}
