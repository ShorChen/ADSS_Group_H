package Employees.Service;

import java.util.*;

import Employees.DataAccess.Entities.EmployeeEntity;
import Employees.DataAccess.Pools.EmployeePool;
import Employees.Domain.Entities.Employee;
import Employees.Domain.Entities.Role;
import Employees.Domain.Entities.ShiftKey;
import Employees.Shared.Enums.WeekDay;
import Employees.Domain.Utils.PasswordGenerator;
import Employees.Shared.Enums.ShiftType;

public class EmployeeService {

    private final EmployeePool employees;

    public EmployeeService() {
        this.employees = EmployeePool.Instance();
    }

    public String addEmployee(Employee employee) {
        String pass = PasswordGenerator.generatePassword();
        if (!employees.exists(employee.getId())) {
            employees.addUpdateEmployee(employee.toEntity(pass));
            return pass;
        }
        throw new IllegalArgumentException("An employee with the given id is already present in the system");
    }

    public boolean deactivateEmployee(String id) {
        if (employees.exists(id)) {
            EmployeeEntity entity = employees.getEmployee(id);
            if (!entity.active())
                throw new IllegalArgumentException("Employee was already not active");
            employees.deactivateEmployee(id);
            return true;
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

    public boolean containsEmployee(String id) {
        return employees.exists(id);
    }

    public boolean updateEmployee(Employee employee, String password) {
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

    public List<Employee> getAvailableEmployees(WeekDay weekDay, ShiftType shiftType, Role role) {
        List<Employee> employeeList = new ArrayList<>();
        employees.getEmployeesWithRole(role.getTag()).forEach(
                e -> {
                    Employee employee = new Employee(e);
                    if (!employee.getUnavailableShifts()
                            .getOrDefault(weekDay, new HashSet<>()).contains(shiftType))
                        employeeList.add(employee);
                });

        return employeeList;
    }

    public boolean containsRole(String id, Role role) {
        if (!employees.exists(id))
            throw new IllegalArgumentException("Employee Not Found");
        return employees.getEmployee(id).qualifiedRoles().contains(role.getTag());
    }
}
