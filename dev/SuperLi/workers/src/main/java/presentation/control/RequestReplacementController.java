package presentation.control;

import context.SessionManager;
import domain.entities.Request;
import domain.entities.Role;
import domain.entities.Shift;
import domain.entities.ShiftKey;
import domain.services.EmployeeService;
import domain.services.RequestReplacementService;
import domain.services.ShiftService;
import domain.util.RequestStateMachine;
import presentation.model.RequestPL;
import presentation.model.ShiftPL;
import shared.enums.RequestStatus;
import shared.enums.ShiftType;
import shared.enums.WeekConstants;
import shared.enums.WeekDay;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestReplacementController {
    private final RequestReplacementService service;
    private final EmployeeService employeeService;
    private final ShiftService shiftService;
    private final AuthController authController;

    public RequestReplacementController() {
        service = new RequestReplacementService();
        employeeService = new EmployeeService();
        shiftService = new ShiftService();
        authController = new AuthController();
    }

    public void currentEmployeeRequestShiftReplacement(WeekDay day, ShiftType type, String otherId) {

        LocalDate targetDate = SessionManager.now().plusWeeks(1).toLocalDate();
        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getCurrentEmployee().getBranchId();

        Map<ShiftKey, Shift> map = shiftService.getShiftsOfWeek(branchId, year, week);
        Shift shift = map.get(new ShiftKey(day, type));

        if (shift.doesEmployeeWork(otherId))
            throw new IllegalArgumentException("Employee already in enrolled in that shift");

        String id = SessionManager.getCurrentEmployee().getId();
        Role role = shift.getEmployeeShiftRole(id);

        if (employeeService.containsRole(otherId, role)) {
            Request r = new Request(shift, id, otherId);
            service.requestReplacement(r);
        }
        throw new IllegalArgumentException("other employee is not qualified for your job");
    }

    public List<RequestPL> getCurrentEmployeePendingRequests() {
        String id = SessionManager.getCurrentEmployee().getId();
        List<Request> requests = service.getPendingRequests(id);
        if (authController.isManager(id)) requests = service.getAllRequests();

        List<RequestPL> pendingRequests = new ArrayList<>();
        requests.forEach(request -> pendingRequests.add(new RequestPL(request)));
        return pendingRequests;
    }

    /**
     * the method attempts to change the status of the current employee
     * in regard to the request {@code request} ,given as a parameter, to 'approved'
     *
     * @param request the request to be approved by the current employee
     * @return true if and only if the request was not approved by the current employee
     * prior to the method call.
     */
    public boolean currentEmployeeApproved(RequestPL request) {
        return service.approve(request.toRequest(),
                SessionManager.getCurrentEmployee().getId());
    }

    public boolean doAllSidesApprove(RequestPL requestPL) {
        return requestPL.toRequest().isApproved();
    }

    public boolean currentEmployeeDenied(RequestPL request) {
        return service.deny(request.toRequest(),
                SessionManager.getCurrentEmployee().getId());
    }

    public boolean getCurrentEmployeeShiftsPredicate(ShiftPL shiftPL) {
        String id = SessionManager.getCurrentEmployee().getId();
        boolean isManager = authController.isManager(id);

        return shiftPL != null &&
               (shiftPL.toShift().doesEmployeeWork(id) || isManager);
    }

//    public void completeRequest(RequestPL request) {
//        LocalDate weekDate = SessionManager.now().toLocalDate();
//        WeekShifts weekShifts = shiftService.getNWeeksAgo(weekDate);
//
//        Shift shift = request.getShift().toShift();
//        weekShifts.addUpdateShift(shift);
//        shiftService.updateWeek(weekShifts);
//    }

    public void deleteRequest(RequestPL request) {
        // todo: implement.
    }

    @Deprecated
    void t(Request request, RequestStatus status){
        RequestStateMachine stateMachine = new RequestStateMachine();
        stateMachine.apply(new RequestStateMachine.State(request.getPrevStatus(),
                        request.getNewStatus(), request.getManagerStatus()),
                new RequestStateMachine.Letter(RequestStateMachine.Player.NEW_EMPLOYEE,
                        RequestStatus.ACCEPT));
    }

    public void completeRequest(RequestPL request) {
        // todo: implement
    }
}
