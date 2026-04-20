package domain.services;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

import data_access.entities.EmployeeEntity;
import data_access.pools.EmployeePool;
import domain.entities.Employee;
import domain.util.PasswordGenerator;
import domain.enums.ShiftType;

public class EmployeeService {

    private final EmployeePool employees;

    public EmployeeService() {
        this.employees = EmployeePool.Instance();
    }

    public String addEmployee(Employee employee) {
        String pass = PasswordGenerator.generatePassword();
        if (!employees.containsEmployee(employee.getId())) {
            employees.addEmployee(employee.toEntity(pass));
            return pass;
        }
        throw new IllegalArgumentException("An employee with the given id is already present in the system");
    }

    public boolean deactivateEmployee(String id) {
        if (employees.exists(id)) {
            EmployeeEntity entity = employees.getEmployee(id);
            if (entity.isActive()) {
                entity.setActive(false);
                return true;
            }
        }
        return false;
    }

    public Employee getEmployeeDetails(String id) {
        if (employees.exists(id)) {
            EmployeeEntity entity = employees.getEmployee(id);
            return new Employee(entity);
        }
        return null;
    }

    public boolean updateEmployee(Employee employee, String password) {
        return employees.getEmployee(employee.getId()).update(employee.toEntity(password));
    }

    public void updateAvailability(String id, Map<domain.enums.WeekDay, Set<domain.enums.ShiftType>> shifts, boolean canWorkDoubleShifts) {
        if (!employees.exists(id)) {
            throw new IllegalArgumentException("Employee with ID " + id + " not found");
        }

        EmployeeEntity entity = employees.getEmployee(id);

        Map<Integer, Set<Integer>> entityShifts = new HashMap<>();

        if (shifts != null) {
            shifts.forEach((day, types) -> {
                Set<Integer> shiftInts = new HashSet<>();
                for (ShiftType type : types) {
                    shiftInts.add(type.ordinal());
                }
                entityShifts.put(day.ordinal(), shiftInts);
            });
        }

        entity.setUnavailableShifts(entityShifts);
        entity.setWorkingDoubles(canWorkDoubleShifts);

    }
}
