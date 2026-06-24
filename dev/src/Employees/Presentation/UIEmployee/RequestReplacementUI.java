package Employees.Presentation.UIEmployee;

import Employees.Presentation.Controller.RequestReplacementController;
import Employees.Presentation.DTO.RequestPL;
import Employees.Presentation.UIShared.ShiftsView;
import Employees.Presentation.UIShared.ViewCLI;
import Employees.Presentation.Utils.Option;

public class RequestReplacementUI extends ViewCLI {
    private boolean open;
    private final RequestReplacementController controller;
    private ShiftsView shiftsView;

    public RequestReplacementUI(Runnable onDismiss) {
        super(onDismiss);
        controller = new RequestReplacementController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {

            boolean isFirstWeek = controller.isFirstWeek();
            if (isFirstWeek) onFirstWeek();
            else shiftsView = new ShiftsView(0, controller::getCurrentEmployeeShiftsPredicate);

            shiftsView.display();
            System.out.println("X - You were not placed for this shift\n");


            Option.Builder builder = new Option.Builder("--- Requests ---")
                    .append("Back", onDismiss)
                    .append("Create new request", this::createRequest);
            controller.getCurrentEmployeePendingRequests().forEach(req ->
                    builder.append(req.toString(), () -> chooseAction(req)));


            displayMenu(builder);
        }
    }

    private void chooseAction(RequestPL request) {
        displayMenu(new Option.Builder("Request: " + request)
                .append("Back", () -> {
                })
                .append("Approve", () -> onRequestApproved(request))
                .append("Deny", () -> onRequestDenied(request)));
    }

    private void onRequestApproved(RequestPL request) {
        boolean approved = controller.currentEmployeeApproved(request);

        if (approved && controller.doAllSidesApprove(request)) {
            controller.completeRequest(request);
            System.out.println("All sides approved, completing request");
        }
    }

    private void onRequestDenied(RequestPL request) {
        boolean denied = controller.currentEmployeeDenied(request);
        if (denied) {
            controller.deleteRequest(request);
            System.out.println("Denied, deleting request");
        } else System.out.println("Request was already denied");
    }

    private void createRequest() {
        shiftsView.selectShiftOfPredicate((day, type) -> {
            String otherId = getNextLine("Enter employee's id to replace shift with");

            try {
                controller.currentEmployeeRequestShiftReplacement(day, type, otherId);
                System.out.println("Request Submitted");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

        });
    }

    private void onFirstWeek() {
        Option.Builder builder = new Option.Builder("Select Week");
        builder.append("Back", onDismiss);
        builder.append("This Week", () -> shiftsView = new ShiftsView(0,
                controller::getCurrentEmployeeShiftsPredicate));
        builder.append("Upcoming Week", () -> shiftsView = new ShiftsView(-1,
                controller::getCurrentEmployeeShiftsPredicate));
        displayMenu(builder);
    }

    @Override
    public void close() {
        open = false;
    }
}
