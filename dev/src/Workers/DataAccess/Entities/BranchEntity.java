package Workers.DataAccess.Entities;

import Workers.DataAccess.Entities.Keys.WeekKey;

public record BranchEntity(int branchId, String location, String branchManagerId, WeekKey startDate) {

}

