package Employees.Domain.Entities;

public class BranchDL {
    public static final int ALL_BRANCHES = -1;
    private int branchId;
    private String location;
    private String branchManagerId;
    private int year;
    private int week;

    public BranchDL(int branchId, String location, String branchManagerId, int year, int week) {
        this.branchId = branchId;
        this.location = location;
        this.branchManagerId = branchManagerId;
        this.year = year;
        this.week = week;
    }

    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getBranchManagerId() { return branchManagerId; }
    public void setBranchManagerId(String branchManagerId) { this.branchManagerId = branchManagerId; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getWeek() { return week; }
    public void setWeek(int week) { this.week = week; }
}