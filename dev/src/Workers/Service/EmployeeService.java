package Workers.Service;

import java.util.*;

import Workers.DataAccess.DAO.EmployeeDAO;
import Workers.DataAccess.Entities.EmployeeEntity;
import Workers.DataAccess.Pools.EmployeePool;
import Workers.Domain.DTO.EmployeeSL;
import Workers.Domain.DTO.RoleSL;
import Workers.Domain.DTO.ShiftKey;
import Workers.Shared.Enums.WeekDay;
import Workers.Domain.Utils.PasswordGenerator;
import Workers.Shared.Enums.ShiftType;

public class EmployeeService {
    private final EmployeeDAO employees;

    public EmployeeService() {
        this.employees = EmployeePool.Instance();
    }

    public String addEmployee(EmployeeSL employee) {
        String pass = PasswordGenerator.generatePassword();
        if (!employees.exists(employee.getId())) {
            employees.addUpdateEmployee(employee.toEntity(pass));
            return pass;
        }
        throw new IllegalArgumentException("An employee with the given id is already present in the system");
    }

    public boolean deactivateEmployee(int branchId, String id) {
        if (employees.exists(id)) {
            EmployeeEntity entity = employees.getEmployee(id);
            if (!entity.active())
                throw new IllegalArgumentException("Employee was already not active");
            if (entity.branchId() != branchId)
                throw new IllegalArgumentException("Employee works at branch " + entity.branchId() + ", while you are viewing " + branchId);
            employees.deactivateEmployee(id);
            return true;
        }
        return false;
    }

    public EmployeeSL getEmployeeDetails(String id) {
        if (employees.exists(id)) {
            EmployeeEntity entity = employees.getEmployee(id);
            return new EmployeeSL(entity);
        }
        return null;
    }

    public boolean containsEmployee(String id) {
        return employees.exists(id);
    }

    public boolean updateEmployee(EmployeeSL employee, String password) {
        if (!employees.exists(employee.getId())) {
            return false;
        }
        employees.addUpdateEmployee(employee.toEntity(password));
        return true;
    }

    public void updateAvailability(String id, Set<ShiftKey> shifts, boolean canWorkDoubleShifts) {
        if (!employees.exists(id)) {
            throw new IllegalArgumentException("Employee with ID " + id + " not found");
        }
        EmployeeEntity entity = employees.getEmployee(id);

        Map<Integer, Set<Integer>> entityShifts = new HashMap<>();
        shifts.forEach(shiftKey -> {

            Set<Integer> defaultSet = new HashSet<>();
            defaultSet.add(ShiftType.number(shiftKey.shiftType()));

            entityShifts.put(WeekDay.number(shiftKey.day()),
                    entityShifts.getOrDefault(WeekDay.number(shiftKey.day()), defaultSet)
            );
        });
        entity = entity.changeAvailability(entityShifts, canWorkDoubleShifts);
        employees.addUpdateEmployee(entity);
    }

    public List<EmployeeSL> getAvailableEmployees(WeekDay weekDay, ShiftType shiftType, RoleSL role) {
        List<EmployeeSL> employeeList = new ArrayList<>();
        employees.getEmployeesWithRole(role.getTag()).forEach(
                e -> {
                    EmployeeSL employee = new EmployeeSL(e);
                    if (!employee.getUnavailableShifts()
                            .getOrDefault(weekDay, new HashSet<>()).contains(shiftType))
                        employeeList.add(employee);
                });

        return employeeList;
    }

    public boolean containsRole(String id, RoleSL role) {
        if (!employees.exists(id))
            throw new IllegalArgumentException("Employee Not Found");
        return employees.getEmployee(id).qualifiedRoles().contains(role.getTag());
    }
}
