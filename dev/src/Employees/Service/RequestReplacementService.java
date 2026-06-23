package Employees.Service;

import Employees.DataAccess.RequestDAO;
import Employees.DataAccess.Entities.RequestEntity;
import Employees.DataAccess.Pools.RequestsPool;
import Employees.Domain.DTO.RequestSL;
import Employees.Domain.Utils.RequestStateMachine;
import org.jetbrains.annotations.NotNull;
import Employees.Shared.Enums.RequestStatus;

import java.util.ArrayList;
import java.util.List;

public class RequestReplacementService {
    private final RequestDAO requestsPool;

    public RequestReplacementService() {
        requestsPool = RequestsPool.Instance();
    }

    public boolean requestReplacement(@NotNull RequestSL request) {
        RequestEntity entity = request.toEntity();
        boolean exists = requestsPool.exists(entity);
        requestsPool.addUpdateRequest(entity);
        return exists;
    }

    public List<RequestSL> getPendingRequests(String id) {
        List<RequestSL> pending = new ArrayList<>();
        requestsPool.getPendingRequests(id).forEach(entity -> {
            pending.add(new RequestSL(entity));
        });
        return pending;
    }

    public boolean approve(@NotNull RequestSL request, String id) {
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

    public boolean deny(@NotNull RequestSL request, String id) {
        request.deny(id);
        if (!request.isDenied()) return false;
        requestsPool.addUpdateRequest(request.toEntity());
        return true;
    }

    public List<RequestSL> getAllRequests() {
        List<RequestSL> list = new ArrayList<>();
        requestsPool.getAll().forEach(r -> list.add(new RequestSL(r)));
        return list;
    }

    public boolean doAllSidesApprove(@NotNull RequestSL request) {
        return request.isApproved();
    }
}
