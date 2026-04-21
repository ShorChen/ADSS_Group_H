package data_access.pools;

import data_access.entities.RequestEntity;
import domain.entities.Request;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;

public class RequestsPool {
    private final Map<LocalDateTime, RequestEntity> requests;

    private static RequestsPool instance;

    public static RequestsPool Instance() {
        if (instance == null)
            instance = new RequestsPool();
        return instance;
    }

    private RequestsPool() {
        this.requests = new HashMap<>();
    }

    public RequestEntity getRequest(int requestId) {
        return requests.getOrDefault(requestId, null);
    }

    public boolean addRequest(RequestEntity request) {
        return requests.put(request.getShift().getStartDate(), request) == null;
    }

    public List<RequestEntity> getPendingRequests(String id) {
        List<RequestEntity> requestEntities = new ArrayList<>();
        requests.forEach((_, entity) -> {
            if (Objects.equals(id, entity.getPrevEmployee()) ||
                Objects.equals(id, entity.getNewEmployee()) ||
                Objects.equals(id, entity.getManager()))
                requestEntities.add(entity);
        });

        return requestEntities;
    }

    public void updateRequest(RequestEntity entity) {
        requests.put(entity.getShift().getStartDate(), entity);
    }

    public void clear() {
        if (this.requests != null) {
            this.requests.clear();
        }
    }
}
