package domain.services;

import data_access.entities.EmployeeEntity;
import data_access.entities.RequestEntity;
import data_access.entities.ShiftEntity;
import data_access.entities.WeekShiftsEntity;
import data_access.pools.EmployeePool;
import data_access.pools.RequestsPool;
import data_access.pools.RolePool;
import data_access.pools.ShiftPool;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataService {


    private EmployeePool employeePool;
    private RequestsPool requestsPool;
    private ShiftPool shiftPool;
    private RolePool rolePool;

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
                "SUNDAY",
                "DAY",
                empsOfShift(),
                new HashMap<>()
        );

        ShiftEntity m2 = new ShiftEntity(
                LocalDateTime.of(2026, 4, 20, 14, 0),
                "MONDAY",
                "EVENING",
                empsOfShift(),
                new HashMap<>()
        );


        WeekShiftsEntity w1 = new WeekShiftsEntity(
                LocalDate.of(2026, 4, 19),
                new HashMap<>(), new HashMap<>()
        );
        w1.addDayShift(s1.getDay(), s1);

        WeekShiftsEntity w2 = new WeekShiftsEntity(
                LocalDate.of(2026, 4, 12),
                new HashMap<>(), new HashMap<>()
        );

        WeekShiftsEntity w3 = new WeekShiftsEntity(
                LocalDate.of(2026, 3, 29),
                new HashMap<>(), new HashMap<>()
        );
        w3.addNightShift(m2.getDay(), m2);

        shiftPool.updateWeek(w1);
        shiftPool.updateWeek(w2);
        shiftPool.updateWeek(w3);

        requestsPool.addRequest(
                new RequestEntity(s1, "id 1", "id 4"));
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
        EmployeeEntity[] employeeEntities = new EmployeeEntity[8];
        String[] roles = {"Cashier", "Shift Manager", "Cleaner", "Security", "Cashier", "Intern"
                , "Storekeeper", "Storekeeper"};
        for (int i = 0; i < employeeEntities.length; i++) {
            StringBuilder pass = new StringBuilder().append(i).append(i).append(i).append(i);
            employeeEntities[i] = new EmployeeEntity("id " + i, pass.toString());
            employeeEntities[i].setQualifiedRoles(roles[i]);
            employeePool.addEmployee(employeeEntities[i]);
        }
    }


    private void loadRoles() {
        rolePool.addRole("Cleaner");
        rolePool.addRole("Security");
        rolePool.addRole("Intern");
    }
}
