package Employees.DataAccess.Pools;

import Employees.DataAccess.RequestDAO;
import Employees.DataAccess.Entities.RequestEntity;
import Employees.DataAccess.Entities.Keys.RequestKey;

import java.util.*;

public class RequestsPool implements RequestDAO {
    private final Map<RequestKey, RequestEntity> requests;

    private static RequestsPool instance;

    public static RequestsPool Instance() {
        if (instance == null)
            instance = new RequestsPool();
        return instance;
    }

    private RequestsPool() {
        this.requests = new HashMap<>();
    }

    public void addUpdateRequest(RequestEntity request) {
        requests.put(createKey(request.requestId(), request.shift().shiftId()), request);
    }

    public boolean exists(RequestEntity request) {
        return requests.containsKey(createKey(request.requestId(), request.shift().shiftId()));
    }

    public List<RequestEntity> getPendingRequests(String id) {
        List<RequestEntity> requestEntities = new ArrayList<>();
        requests.forEach((_, entity) -> {
            if (Objects.equals(id, entity.prevEmployee()) ||
                Objects.equals(id, entity.newEmployee()) ||
                Objects.equals(id, entity.manager()))
                requestEntities.add(entity);
        });

        return requestEntities;
    }

    public List<RequestEntity> getAll() {
        return new ArrayList<>(requests.values());
    }

    public static void free(){
        instance = null;
    }

    private RequestKey createKey(int requestId, int shiftId) {
        return new RequestKey(requestId, shiftId);
    }
}
