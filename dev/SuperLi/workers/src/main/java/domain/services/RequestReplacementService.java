package domain.services;

import data_access.pools.RequestsPool;
import data_access.pools.ShiftPool;
import domain.entities.Request;
import domain.entities.Shift;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class RequestReplacementService {
    private final RequestsPool requestsPool;
    private final ShiftPool shiftPool;

    public RequestReplacementService() {
        requestsPool = RequestsPool.Instance();
        shiftPool = ShiftPool.Instance();

    }

    public Shift getShift(@NotNull LocalDate date, @NotNull WeekDay day, @NotNull ShiftType type) {
        LocalDate weekDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        return new Shift(shiftPool.getShift(weekDate, day.name(), type.name()));
    }

    public boolean requestReplacement(@NotNull Request request) {
        return requestsPool.addRequest(request.toEntity());
    }

    public List<Request> getPendingRequests(String id) {
        List<Request> pending = new ArrayList<>();
        requestsPool.getPendingRequests(id).forEach(entity -> {
            pending.add(new Request(entity));
        });
        return pending;
    }

    public boolean approve(@NotNull Request request, String id) {
        boolean statusChanged = request.approve(id);
        requestsPool.updateRequest(request.toEntity());
        return statusChanged;
    }

    public boolean deny(@NotNull Request request, String id) {
        if (!request.deny(id)) return false;
        requestsPool.updateRequest(request.toEntity());
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
