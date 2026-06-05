package presentation.control;

import context.SessionManager;
import domain.entities.Request;
import domain.entities.Shift;
import domain.entities.WeekShifts;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import domain.services.EmployeeService;
import domain.services.RequestReplacementService;
import domain.services.ShiftService;
import presentation.model.RequestPL;
import presentation.model.ShiftPL;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public void currentEmployeeRequestShiftReplacement(int day, int type, String otherId) {

        Shift shift = service.getShift(SessionManager.now().toLocalDate(),
                WeekDay.values()[day],
                ShiftType.values()[type]);
        ShiftPL shiftPL = new ShiftPL(shift);

        if (shiftPL.find(otherId) != null)
            throw new IllegalArgumentException("Employee already in enrolled in that shift");

        String id = SessionManager.getCurrentEmployee().getId();
        String role = shiftPL.find(id);

        if (employeeService.containsRole(otherId, role)) {
            RequestPL r = new RequestPL(shiftPL, id, otherId);
            service.requestReplacement(r.toRequest());
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
        return service.doAllSidesApprove(requestPL.toRequest());
    }

    public boolean currentEmployeeDenied(RequestPL request) {
        return service.deny(request.toReplacementRequest(),
                SessionManager.getCurrentEmployee().getId());
    }

    public boolean getCurrentEmployeeShiftsPredicate(ShiftPL shiftPL) {
        String id = SessionManager.getCurrentEmployee().getId();
        boolean isManager = authController.isManager(id);
        return shiftPL != null &&
               (shiftPL.find(id) != null || isManager);
    }

    public void completeRequest(RequestPL request) {
        LocalDate weekDate = SessionManager.now().toLocalDate();
        WeekShifts weekShifts = shiftService.getNWeeksAgo(weekDate);
        Shift shift = request.getShift().toShift();
        switch (shift.getShiftType()) {
            case ShiftType.DAY -> weekShifts.addDayShift(shift.getDay(), shift);
            case ShiftType.EVENING -> weekShifts.addNightShift(shift.getDay(), shift);
        }
        shiftService.updateWeek(weekShifts);
    }

    public void deleteRequest(RequestPL request) {
        // todo: implement.
    }
}
