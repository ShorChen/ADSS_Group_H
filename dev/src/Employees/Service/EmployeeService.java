package Employees.Service;

import Employees.DataAccess.EmployeeDAO;
import Employees.DataAccess.Entities.EmployeeEntity;
import Employees.DataAccess.Pools.EmployeePool;
import Employees.DataAccess.SqlImpl.SqlEmployeeDAO;
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
        this.employees = new SqlEmployeeDAO();
    }

    public String addEmployee(EmployeeSL employee) {

        validateEmployeeData(employee);
        if (employees.exists(employee.getId()))
            throw new IllegalArgumentException("Error: An employee with the given ID is already present in the system.");
        String pass = PasswordGenerator.generatePassword();
        employees.addUpdateEmployee(employee.toEntity(pass));
        return pass;
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

        validateEmployeeData(employee);
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

    private void validateEmployeeData(EmployeeSL employee) {
        if (employee.getSalary() <= 0) {
            throw new IllegalArgumentException("Validation Error: Salary must be a positive number.");
        }
        if (employee.getBankAccount() == null || employee.getBankAccount().trim().isEmpty()) {
            throw new IllegalArgumentException("Validation Error: Bank account cannot be empty.");
        }
        if (employee.getQualifiedRoles() == null || employee.getQualifiedRoles().isEmpty()) {
            throw new IllegalArgumentException("Validation Error: An employee must be assigned to at least one Role upon onboarding.");
        }
        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Validation Error: Employee name cannot be empty.");
        }
    }
}
