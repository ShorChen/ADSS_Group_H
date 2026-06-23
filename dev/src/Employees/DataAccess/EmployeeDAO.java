package Employees.DataAccess;

import Employees.DataAccess.Entities.EmployeeEntity;

import java.util.List;

public interface EmployeeDAO {

    void addUpdateEmployee(EmployeeEntity employee);
    EmployeeEntity getEmployee(String id);
    boolean exists(String id);
    boolean updatePassword(String id, String oldPass, String newPass);
    void deactivateEmployee(String id);
    List<EmployeeEntity> getEmployeesWithRole(String roles);
}
