package Employees.Service;

import Employees.DataAccess.EmployeeDAO;
import Employees.DataAccess.Entities.EmployeeEntity;
import Employees.DataAccess.Pools.EmployeePool;
import Employees.Domain.DTO.EmployeeSL;
import Employees.Domain.DTO.RoleSL;
import Employees.Domain.DTO.ShiftKey;
import Employees.Domain.Utils.PasswordGenerator;
import Employees.Domain.DTO.AvailabilitySubmissionSL;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;

import java.util.*;

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

    public void updateAvailability(AvailabilitySubmissionSL availabilitySubmission) {
        String id = availabilitySubmission.getEmployeeId();
        if (!employees.exists(id)) {
            throw new IllegalArgumentException("Employee with ID " + id + " not found");
        }
        EmployeeEntity entity = employees.getEmployee(id);

        entity = entity.changeAvailability(availabilitySubmission.toEntity());
        employees.addUpdateEmployee(entity);
    }

    public List<EmployeeSL> getAvailableEmployees(WeekDay weekDay, ShiftType shiftType, RoleSL role) {
        List<EmployeeSL> employeeList = new ArrayList<>();
        employees.getEmployeesWithRole(role.getTag()).forEach(
                e -> {
                    EmployeeSL employee = new EmployeeSL(e);
                    if (employee.getAvailabilitySubmission()
                            .getShift(new ShiftKey(weekDay, shiftType)))
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
