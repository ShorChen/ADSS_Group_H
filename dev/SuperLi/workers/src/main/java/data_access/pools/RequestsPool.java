package data_access.pools;

import data_access.entities.RequestEntity;

import java.util.ArrayList;
import java.util.List;

public class RequestsPool {
    private final List<RequestEntity> replacementRequests;

    private static RequestsPool instance;
    public static RequestsPool Instance() {
        if (instance == null)
            instance = new RequestsPool();
        return instance;
    }

    private RequestsPool() {
        this.replacementRequests = new ArrayList<>();
    }

    public RequestEntity getRequest(long requestId) {
        for (RequestEntity r : replacementRequests)
            if (r.getId() == requestId)
                return r;
        return null;
    }

}
