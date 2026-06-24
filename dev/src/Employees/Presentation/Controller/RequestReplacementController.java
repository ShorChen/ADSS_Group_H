package Employees.Presentation.Controller;

import Employees.Context.SessionManager;
import Employees.Domain.DTO.RequestSL;
import Employees.Domain.DTO.RoleSL;
import Employees.Domain.DTO.ShiftSL;
import Employees.Domain.DTO.ShiftKey;
import Employees.Service.BranchService;
import Employees.Service.EmployeeService;
import Employees.Service.RequestReplacementService;
import Employees.Service.ShiftService;
import Employees.Domain.Utils.RequestStateMachine;
import Employees.Presentation.DTO.RequestPL;
import Employees.Presentation.DTO.ShiftPL;
import Employees.Shared.Enums.RequestStatus;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.WeekConstants;
import Employees.Shared.Enums.WeekDay;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestReplacementController {
    private final RequestReplacementService service;
    private final EmployeeService employeeService;
    private final ShiftService shiftService;
    private final AuthController authController;
    private final BranchService branchService;

    public RequestReplacementController() {
        service = new RequestReplacementService();
        employeeService = new EmployeeService();
        shiftService = new ShiftService();
        authController = new AuthController();
        branchService = new BranchService();
    }

    public void currentEmployeeRequestShiftReplacement(WeekDay day, ShiftType type, String otherId) {

        LocalDate targetDate = SessionManager.now().plusWeeks(1).toLocalDate();
        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getSelectedBranchId();

        Map<ShiftKey, ShiftSL> map = shiftService.getShiftsOfWeek(branchId, year, week);
        ShiftSL shift = map.get(new ShiftKey(day, type));

        if (shift.doesEmployeeWork(otherId))
            throw new IllegalArgumentException("Employee already in enrolled in that shift");

        String id = SessionManager.getCurrentEmployee().getId();
        RoleSL role = shift.getEmployeeShiftRole(id);

        if (employeeService.containsRole(otherId, role)) {
            RequestSL r = new RequestSL(shift, id, otherId);
            service.requestReplacement(r);
        }
        throw new IllegalArgumentException("other employee is not qualified for your job");
    }

    public List<RequestPL> getCurrentEmployeePendingRequests() {
        String id = SessionManager.getCurrentEmployee().getId();
        List<RequestSL> requests = service.getPendingRequests(id);
        if (authController.isManager(id) || authController.isBranchManager(id))
            requests = service.getAllRequests();

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
    void t(RequestSL request, RequestStatus status) {
        RequestStateMachine stateMachine = new RequestStateMachine();
        stateMachine.apply(new RequestStateMachine.State(request.getPrevStatus(),
                        request.getNewStatus(), request.getManagerStatus()),
                new RequestStateMachine.Letter(RequestStateMachine.Player.NEW_EMPLOYEE,
                        RequestStatus.ACCEPT));
    }

    public void completeRequest(RequestPL requestPL) {
        RequestSL request = requestPL.toRequest();
        ShiftSL s = request.getShift();
        s.replaceEmployees(request.getRole(), request.getPrevEmployee(),
                request.getNewEmployee());

        shiftService.addUpdateShift(
                request.getBranchId(),
                request.getYear(),
                request.getWeek(),
                request.getShift().getDay().toString(),
                request.getShift().getShiftType().toString(),
                s
        );
    }

    public boolean isFirstWeek() {
        LocalDate targetDate = SessionManager.now().toLocalDate();

        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getSelectedBranchId();

        return branchService.isFirstWeek(branchId, year, week);
    }
}
