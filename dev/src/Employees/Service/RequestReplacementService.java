package Employees.Service;

import Employees.DataAccess.Entities.RequestEntity;
import Employees.DataAccess.Pools.RequestsPool;
import Employees.Domain.Entities.Request;
import Employees.Domain.Utils.RequestStateMachine;
import org.jetbrains.annotations.NotNull;
import Employees.Shared.Enums.RequestStatus;

import java.util.ArrayList;
import java.util.List;

public class RequestReplacementService {
    private final RequestsPool requestsPool;

    public RequestReplacementService() {
        requestsPool = RequestsPool.Instance();
    }

    public boolean requestReplacement(@NotNull Request request) {
        RequestEntity entity = request.toEntity();
        boolean exists = requestsPool.exists(entity);
        requestsPool.addUpdateRequest(entity);
        return exists;
    }

    public List<Request> getPendingRequests(String id) {
        List<Request> pending = new ArrayList<>();
        requestsPool.getPendingRequests(id).forEach(entity -> {
            pending.add(new Request(entity));
        });
        return pending;
    }

    public boolean approve(@NotNull Request request, String id) {
        RequestStateMachine stateMachine = new RequestStateMachine();
        RequestStateMachine.State startState = new RequestStateMachine.State(
                request.getPrevStatus(),
                request.getNewStatus(),
                request.getManagerStatus());

        RequestStateMachine.State endState = stateMachine.apply(startState, new RequestStateMachine.Letter(
                RequestStateMachine.Player.NEW_EMPLOYEE, RequestStatus.ACCEPT)
        );
        boolean statusChanged = endState.equals(startState);

        requestsPool.addUpdateRequest(request.toEntity());
        return statusChanged;


        // TODO: this does not work
    }

    public boolean deny(@NotNull Request request, String id) {
        request.deny(id);
        if (!request.isDenied()) return false;
        requestsPool.addUpdateRequest(request.toEntity());
        return true;
    }

    public List<Request> getAllRequests() {
        List<Request> list = new ArrayList<>();
        requestsPool.getAll().forEach(r -> list.add(new Request(r)));
        return list;
    }

    public boolean doAllSidesApprove(@NotNull Request request) {
        return request.isApproved();
    }
}
