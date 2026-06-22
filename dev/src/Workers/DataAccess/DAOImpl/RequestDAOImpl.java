package Workers.DataAccess.DAOImpl;

import Workers.DataAccess.DAO.RequestDAO;
import Workers.DataAccess.Entities.RequestEntity;

import java.util.List;

public class RequestDAOImpl implements RequestDAO {
    @Override
    public void addUpdateRequest(RequestEntity request) {

    }

    @Override
    public boolean exists(RequestEntity request) {
        return false;
    }

    @Override
    public List<RequestEntity> getPendingRequests(String id) {
        return List.of();
    }

    @Override
    public List<RequestEntity> getAll() {
        return List.of();
    }
}
