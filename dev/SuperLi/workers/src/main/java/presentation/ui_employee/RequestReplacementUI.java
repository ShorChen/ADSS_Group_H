package presentation.ui_employee;

import context.SessionManager;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import presentation.control.RequestReplacementController;
import presentation.model.RequestPL;
import presentation.ui_shared.ShiftsView;
import presentation.ui_shared.View;
import presentation.util.Option;

import java.util.List;
import java.util.Map;

public class RequestReplacementUI extends View {
    private boolean open;
    private final RequestReplacementController controller;

    public RequestReplacementUI(Runnable onDismiss) {
        super(onDismiss);
        controller = new RequestReplacementController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {

            List<RequestPL> requests = controller.getPendingRequests(
                    SessionManager.getCurrentEmployee().getId()
            );
            Option.Builder builder = new Option.Builder("--- Requests ---")
                    .append("Back", onDismiss)
                    .append("Create new request", this::createRequest);
            requests.forEach(req ->
                    builder.append(req.toString(), () -> chooseAction(req)));

            displayMenu(builder, "");
        }
    }

    private void chooseAction(RequestPL request) {
        displayMenu(new Option.Builder("Request: " + request)
                        .append("Approve", () -> {
                            boolean allApprove = controller.approve(request,
                                    SessionManager.getCurrentEmployee().getId());

                            if (allApprove)
                                System.out.println("All sides approved, completing request");
                        })
                        .append("Deny", () -> {
                            controller.deny(request,
                                    SessionManager.getCurrentEmployee().getId());
                            System.out.println("Denied, deleting request");
                        }),
                "");
    }

    private void createRequest() {
        ShiftsView shiftsView = new ShiftsView(0);

        WeekDay[] day = new WeekDay[1];
        ShiftType[] type = new ShiftType[1];

        Map<WeekDay, List<ShiftType>> workingShifts = controller.getWorkingShifts(
                SessionManager.getCurrentEmployee().getId());

        final Option.Builder builderDays = new Option.Builder("Enter Shift's Week Day");
        workingShifts.forEach((weekDay, shifts) -> {
            shifts.forEach(shiftType ->
                    shiftsView.setMarked(weekDay.ordinal(), shiftType.ordinal(), ' '));
            if (!shifts.isEmpty())
                builderDays.append(weekDay.day, () -> day[0] = weekDay);
        });
        if (builderDays.size() == 0) {
            System.out.println("You were not placed to any shift this week");
            return;
        }

        shiftsView.display();
        System.out.println("X - No shift\n");

        displayMenu(builderDays, "");
        final Option.Builder builderShifts = new Option.Builder("Enter Shift's Week Day");
        workingShifts.get(day[0]).forEach(shiftType ->
                builderShifts.append(shiftType.type, () -> type[0] = shiftType));

        if (builderShifts.size() == 0) {
            System.out.println("You do not have any shifts for this day");
            return;
        }

        displayMenu(builderShifts, "");

        String otherId = getNextLine("Enter employee's id to replace shift with");

        try {
            boolean request = controller.requestShiftReplacement(day, type,
                    SessionManager.getCurrentEmployee().getId(), otherId);

            if (request) System.out.println("Request Submitted");
            else System.out.println("Could not submit request");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public void close() {
        open = false;
    }
}
