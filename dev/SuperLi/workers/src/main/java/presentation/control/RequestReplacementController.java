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

    public boolean requestShiftReplacement(int day, int type, String id,
                                           String otherId) {

        Shift shift = service.getShift(SessionManager.now().toLocalDate(),
                WeekDay.values()[day],
                ShiftType.values()[type]);
        ShiftPL shiftPL = new ShiftPL(shift);

        if (shiftPL.find(otherId) != null)
            throw new IllegalArgumentException("Employee already in enrolled in that shift");

        String role = shiftPL.find(id);

        if (employeeService.containsRole(otherId, role)) {
            RequestPL r = new RequestPL(shiftPL, id, otherId);
            return service.requestReplacement(r.toRequest());
        }
        return false;
    }

    public List<RequestPL> getPendingRequests(String id) {
        List<Request> requests = service.getPendingRequests(id);
        if (authController.isManager(id)) requests = service.getAllRequests();

        List<RequestPL> pendingRequests = new ArrayList<>();
        requests.forEach(request -> pendingRequests.add(new RequestPL(request)));
        return pendingRequests;
    }

    public boolean approve(RequestPL request, String id) {
        if (!service.approve(request.toRequest(), id)) return false;


        LocalDate weekDate = SessionManager.now().toLocalDate();
        WeekShifts weekShifts = shiftService.getNWeeksAgo(weekDate);
        Shift shift = request.getShift().toShift();
        switch (shift.getShiftType()) {
            case ShiftType.DAY -> weekShifts.addDayShift(shift.getDay(), shift);
            case ShiftType.EVENING -> weekShifts.addNightShift(shift.getDay(), shift);
        }
        shiftService.updateWeek(weekShifts);

        return true;
    }

    public boolean deny(RequestPL request, String id) {
        return service.deny(request.toReplacementRequest(), id);
    }

    public boolean getEmployeeShiftsPredicate(ShiftPL shiftPL, String id) {
        boolean isManager = authController.isManager(id);
        return shiftPL != null &&
               (shiftPL.find(id) != null || isManager);
    }
}
