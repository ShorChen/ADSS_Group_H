package data_access.pools;

import domain.entities.ExceptionalPlacementRequest;
import domain.entities.ReplacementRequest;

import java.util.ArrayList;
import java.util.List;

public class RequestsPool {
    private final List<ReplacementRequest> replacementRequests;
    private final List<ExceptionalPlacementRequest> exceptionalPlacementRequests;

    private static RequestsPool instance;
    public static RequestsPool Instance() {
        if (instance == null)
            instance = new RequestsPool();
        return instance;
    }

    private RequestsPool() {
        this.replacementRequests = new ArrayList<>();
        this.exceptionalPlacementRequests =new ArrayList<>();
    }

    public ReplacementRequest getRequest(long requestId) {
        for (ReplacementRequest r : replacementRequests)
            if (r.getRequestId() == requestId)
                return r;
        return null;
    }
    public ExceptionalPlacementRequest getExceptionalRequest(long requestId) {
        for (ExceptionalPlacementRequest r : exceptionalPlacementRequests)
            if (r.getRequestId() == requestId)
                return r;
        return null;
    }

}
