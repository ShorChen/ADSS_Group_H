package Workers.Service;

import Workers.DataAccess.DAO.EmployeeDAO;
import Workers.DataAccess.DAO.RequestDAO;
import Workers.DataAccess.DAO.RoleDAO;
import Workers.DataAccess.DAO.ShiftDAO;
import Workers.DataAccess.Entities.EmployeeEntity;
import Workers.DataAccess.Entities.RequestEntity;
import Workers.DataAccess.Entities.ShiftEntity;
import Workers.DataAccess.Entities.Keys.BranchWeekKey;
import Workers.DataAccess.Entities.Keys.ShiftEntityKey;
import Workers.DataAccess.Pools.EmployeePool;
import Workers.DataAccess.Pools.RequestsPool;
import Workers.DataAccess.Pools.RolePool;
import Workers.DataAccess.Pools.ShiftPool;
import Workers.Shared.Enums.RequestStatus;
import Workers.Shared.Enums.ShiftType;
import Workers.Shared.Enums.WeekDay;

import java.time.LocalDateTime;
import java.util.*;

public class DataService {
    private final EmployeeDAO employeePool;
    private final RequestDAO requestsPool;
    private final ShiftDAO shiftPool;
    private final RoleDAO rolePool;

    public DataService() {
        this.employeePool = EmployeePool.Instance();
        this.requestsPool = RequestsPool.Instance();
        this.shiftPool = ShiftPool.Instance();
        this.rolePool = RolePool.Instance();
    }

    public void load() {
        loadRoles();
        loadEmployees();
        loadShifts();
    }


    private void loadShifts() {
        ShiftEntity s1 = new ShiftEntity(
                LocalDateTime.of(2026, 4, 19, 6, 0),
                WeekDay.SUNDAY.toString(),
                ShiftType.DAY.toString(),
                empsOfShift(),
                new HashMap<>()
        );
        ShiftEntity m2 = new ShiftEntity(
                LocalDateTime.of(2026, 4, 20, 14, 0),
                WeekDay.MONDAY.toString(),
                ShiftType.EVENING.toString(),
                empsOfShift(),
                new HashMap<>()
        );

        shiftPool.addUpdateShift(new BranchWeekKey(1, s1.getYear(), s1.getWeek()),
                new ShiftEntityKey(WeekDay.SUNDAY.toString(), ShiftType.DAY.toString()), s1);
        shiftPool.addUpdateShift(new BranchWeekKey(1, m2.getYear(), m2.getWeek()),
                new ShiftEntityKey(WeekDay.MONDAY.toString(), ShiftType.EVENING.toString()), m2);

        requestsPool.addUpdateRequest(
                new RequestEntity(-1, s1, "id 1", "id 4",
                        null, RequestStatus.ACCEPT.toString(), RequestStatus.PENDING.toString(),
                        RequestStatus.PENDING.toString(), false));
    }

    private Map<String, Set<String>> empsOfShift() {
        Map<String, Set<String>> map = new HashMap<>();
        map.put("Cashier", new HashSet<>());
        map.put("Cleaner", new HashSet<>());
        map.put("Shift Manager", new HashSet<>());

        map.get("Cashier").add("id 1");
        map.get("Shift Manager").add("id 2");

        return map;
    }


    private void loadEmployees() {
        int employeeCount = 8;
        String[] roles = {"Cashier", "Shift Manager", "Cleaner", "Security", "Cashier", "Intern"
                , "Storekeeper", "Storekeeper"};
        for (int i = 0; i < employeeCount; i++) {
            EmployeeEntity e = new EmployeeEntity("id " + i,
                    String.valueOf(i) + i + i + i);

            List<String> newRoles = e.qualifiedRoles();
            newRoles.add(roles[i]);
            employeePool.addUpdateEmployee(e.changeQualifiedRoles(newRoles));
        }
    }


    private void loadRoles() {
        rolePool.addRole("Cleaner");
        rolePool.addRole("Security");
        rolePool.addRole("Intern");
    }
}
