package data_access.pools;

import domain.entities.ExceptionalPlacementRequest;
import domain.entities.ReplacementRequest;

import java.util.List;

public class RequestsPool {
    private final List<ReplacementRequest> replacementRequests;
    private final List<ExceptionalPlacementRequest> exceptionalPlacementRequests;

    public RequestsPool(List<ReplacementRequest> replacementRequests, List<ExceptionalPlacementRequest> exceptionalPlacementRequests) {
        this.replacementRequests = replacementRequests;
        this.exceptionalPlacementRequests = exceptionalPlacementRequests;
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
