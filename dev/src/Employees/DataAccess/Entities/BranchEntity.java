package Employees.DataAccess.Entities;

import Employees.DataAccess.Entities.Keys.WeekKey;

public record BranchEntity(int branchId, String location, String branchManagerId, WeekKey startDate) {

}
