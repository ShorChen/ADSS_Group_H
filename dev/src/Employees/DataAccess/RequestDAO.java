package Employees.DataAccess;

import Employees.DataAccess.Entities.RequestEntity;
import java.util.List;

public interface RequestDAO {
    void addUpdateRequest(RequestEntity request);

    boolean exists(RequestEntity request);

    List<RequestEntity> getPendingRequests(String id);

    List<RequestEntity> getAll();
}