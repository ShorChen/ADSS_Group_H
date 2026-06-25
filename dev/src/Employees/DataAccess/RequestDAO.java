package Employees.DataAccess;
import Employees.Domain.Entities.RequestDL;
import java.util.List;

public interface RequestDAO {
    void addUpdateRequest(RequestDL request);
    List<RequestDL> getAll();
}