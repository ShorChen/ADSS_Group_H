package presentation.ui_employee;

import context.SessionManager;
import presentation.control.RequestReplacementController;
import presentation.model.RequestPL;
import presentation.ui_shared.ShiftsView;
import presentation.ui_shared.View;
import presentation.util.Option;

import java.util.List;

public class RequestReplacementUI extends View {
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
            shiftsView = new ShiftsView(0,
                    shiftPL -> controller.getEmployeeShiftsPredicate(shiftPL,
                            SessionManager.getCurrentEmployee().getId()));
            shiftsView.display();
            System.out.println("X - You were not placed for this shift\n");

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
                            boolean denied = controller.deny(request,
                                    SessionManager.getCurrentEmployee().getId());
                            if (denied)
                                System.out.println("Denied, deleting request");
                            else System.out.println("Request was already denied");
                        }),
                "");
    }

    private void createRequest() {
        shiftsView.selectShiftOfPredicate((day, type) -> {
            String otherId = getNextLine("Enter employee's id to replace shift with");

            try {
                boolean request = controller.requestShiftReplacement(day, type,
                        SessionManager.getCurrentEmployee().getId(), otherId);

                if (request) System.out.println("Request Submitted");
                else System.out.println("other employee is not qualified for your job");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

        });
    }

    @Override
    public void close() {
        open = false;
    }
}
