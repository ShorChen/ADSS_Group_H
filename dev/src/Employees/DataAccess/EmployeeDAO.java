package Employees.DataAccess;
import Employees.Domain.Entities.EmployeeDL;
import java.util.List;

public interface EmployeeDAO {
    void addUpdateEmployee(EmployeeDL employee);
    EmployeeDL getEmployee(String id);
    List<EmployeeDL> getAllEmployees();
}