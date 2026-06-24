package Employees.Service;

import Employees.DataAccess.EmployeeDAO;
import Employees.DataAccess.RequestDAO;
import Employees.DataAccess.RoleDAO;
import Employees.DataAccess.ShiftDAO;
import Employees.DataAccess.Entities.EmployeeEntity;
import Employees.DataAccess.Entities.RequestEntity;
import Employees.DataAccess.Entities.ShiftEntity;
import Employees.DataAccess.Entities.Keys.BranchWeekKey;
import Employees.DataAccess.Entities.Keys.ShiftEntityKey;
import Employees.DataAccess.Pools.EmployeePool;
import Employees.DataAccess.Pools.RequestsPool;
import Employees.DataAccess.Pools.RolePool;
import Employees.DataAccess.Pools.ShiftPool;
import Employees.Shared.Enums.RequestStatus;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;

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
                new RequestEntity(-1, s1, new BranchWeekKey(1, 2026, 26),"id 1", "id 4",
                        null, RequestStatus.ACCEPT.toString(), RequestStatus.PENDING.toString(),
                        RequestStatus.PENDING.toString(), "Cashier",false));
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
