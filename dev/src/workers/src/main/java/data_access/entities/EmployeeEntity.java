package data_access.entities;

import context.SessionManager;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.*;

public record EmployeeEntity(
        String id,
        String name,
        String bankAccount,
        double salary,
        String salaryType,
        LocalDateTime dateOfEmployment,
        String jobScope,
        List<String> qualifiedRoles,
        String constraints,
        int yearlyRestDays,
        String weeklyRestDay,
        String password,
        boolean workingDoubles,
        Map<Integer, Set<Integer>> unavailableShifts,
        boolean active,
        int branchId
) {
    public EmployeeEntity(String id, String password) {
        this(id, "temp-name", "0000-0000-0000-0000", 100.0,
                "HOURLY",
                SessionManager.now(),
                "FULL_TIME", new ArrayList<>(),
                "not free on weekends", 24,
                "SATURDAY", password, false, new HashMap<>(),
                true, 1
        );
    }

    public EmployeeEntity(String id, String name, String bankAccount, double salary,
                          String salaryType, LocalDateTime dateOfEmployment,
                          String jobScope, List<String> qualifiedRoles,
                          String constraints, int yearlyRestDays,
                          String weeklyRestDay, String password, boolean workingDoubles,
                          @NotNull Map<Integer, Set<Integer>> unavailableShifts, boolean active, int branchId) {
        this.id = id;
        this.name = name;
        this.bankAccount = bankAccount;
        this.salary = salary;
        this.salaryType = salaryType;
        this.dateOfEmployment = dateOfEmployment;
        this.jobScope = jobScope;

        this.qualifiedRoles = new ArrayList<>();
        this.qualifiedRoles.addAll(qualifiedRoles);

        this.constraints = constraints;
        this.yearlyRestDays = yearlyRestDays;
        this.weeklyRestDay = weeklyRestDay;
        this.password = password;
        this.workingDoubles = workingDoubles;
        this.unavailableShifts = new HashMap<>();

        unavailableShifts.forEach((key, value) ->
                this.unavailableShifts.put(key, new HashSet<>(value)));

        this.active = active;
        this.branchId = branchId;
    }

    public EmployeeEntity changePassword(String password) {
        return new EmployeeEntity(id, name, bankAccount, salary, salaryType,
                dateOfEmployment, jobScope, qualifiedRoles,
                constraints, yearlyRestDays, weeklyRestDay, password,
                workingDoubles, unavailableShifts,
                active, branchId);
    }

    public EmployeeEntity changeActivityStatus(boolean active) {
        return new EmployeeEntity(id, name, bankAccount, salary, salaryType,
                dateOfEmployment, jobScope, qualifiedRoles,
                constraints, yearlyRestDays, weeklyRestDay, password,
                workingDoubles, unavailableShifts,
                active, branchId);
    }

    public EmployeeEntity changeAvailability(Map<Integer, Set<Integer>> unavailableShifts, boolean workingDoubles) {
        return new EmployeeEntity(id, name, bankAccount, salary, salaryType,
                dateOfEmployment, jobScope, qualifiedRoles,
                constraints, yearlyRestDays, weeklyRestDay, password,
                workingDoubles, unavailableShifts,
                active, branchId);
    }

    public EmployeeEntity changeQualifiedRoles(List<String> qualifiedRoles) {
        return new EmployeeEntity(id, name, bankAccount, salary, salaryType,
                dateOfEmployment, jobScope, qualifiedRoles,
                constraints, yearlyRestDays, weeklyRestDay, password,
                workingDoubles, unavailableShifts,
                active, branchId);
    }

    public List<String> qualifiedRoles() {
        return new ArrayList<>(qualifiedRoles);
    }

    public Map<Integer, Set<Integer>> unavailableShifts() {
        Map<Integer, Set<Integer>> map = new HashMap<>();
        unavailableShifts.forEach((key, value) ->
                map.put(key, new HashSet<>(value)));
        return map;
    }

    public boolean checkPassword(String password) {
        return Objects.equals(password, this.password);
    }

}
