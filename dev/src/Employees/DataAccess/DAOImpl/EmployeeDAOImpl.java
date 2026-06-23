package Employees.DataAccess.DAOImpl;

import Employees.DataAccess.EmployeeDAO;
import Employees.DataAccess.Entities.EmployeeEntity;

import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {
    @Override
    public void addUpdateEmployee(EmployeeEntity employee) {

    }

    @Override
    public EmployeeEntity getEmployee(String id) {
        return null;
    }

    @Override
    public boolean exists(String id) {
        return false;
    }

    @Override
    public boolean updatePassword(String id, String oldPass, String newPass) {
        return false;
    }

    @Override
    public void deactivateEmployee(String id) {

    }

    @Override
    public List<EmployeeEntity> getEmployeesWithRole(String roles) {
        return List.of();
    }
}
