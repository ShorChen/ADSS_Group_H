package Workers.Presentation.Controller;

import Workers.Context.SessionManager;
import Workers.Domain.DTO.EmployeeSL;
import Workers.Domain.DTO.RoleSL;
import Workers.Domain.DTO.BranchSL;
import Workers.Presentation.DTO.StoreDetailsPL;
import Workers.Service.*;
import Workers.Shared.Enums.JobScope;
import Workers.Shared.Enums.SalaryType;
import Workers.Shared.Enums.WeekDay;
import Workers.Shared.WeekConstants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CreateStoreController {
    private final EmployeeService employeeService;
    private final ShiftService shiftService;
    private final DataService dataService;
    private final StoreDetailsService storeDetailsService;
    private final BranchService branchService;

    public CreateStoreController() {
        this.storeDetailsService = new StoreDetailsService();
        employeeService = new EmployeeService();
        shiftService = new ShiftService();
        dataService = new DataService();
        branchService = new BranchService();
    }

    public String registerManager(String id, String name, String bankAccount, String weekDay) {
        List<RoleSL> roles = new ArrayList<>();
        roles.add(RoleSL.MANAGER);
        return employeeService.addEmployee(new EmployeeSL(
                id, name, bankAccount, 0, SalaryType.GLOBALLY,
                SessionManager.now(), JobScope.FULL_TIME,
                roles, "", 24, WeekDay.fromArgs(weekDay),
                false, new HashMap<>(), true, BranchSL.ALL_BRANCHES
        ));
    }

    public void setClosedDays(List<WeekDay> closeDays) {
        List<String> closed = new ArrayList<>();
        closeDays.forEach(w -> closed.add(w.name()));
        storeDetailsService.setClosedDays(closed);
    }

    public void load() {
        dataService.load();
    }

    public void setStoreDetails(StoreDetailsPL storeDetailsPL) {
        storeDetailsService.addUpdateStoreDetails(storeDetailsPL);
    }

    public void addBranch(String managerId, String location) {
        LocalDate targetDate = SessionManager.now().toLocalDate();

        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        BranchSL branch = new BranchSL(0, location,
                managerId, year, week);
        branchService.addUpdateBranch(branch);
    }

    public boolean containsBranch(String location) {
        return branchService.containsBranchAtLocation(location);
    }

}
