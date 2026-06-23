package Employees.Presentation.UIEmployee;

import Employees.Context.SessionManager;
import Employees.Domain.DTO.ShiftKey;
import Employees.Presentation.Controller.EmployeeShiftController;
import Employees.Presentation.DTO.AvailabilitySubmissionPL;
import Employees.Presentation.UIShared.ShiftsView;
import Employees.Presentation.UIShared.ViewCLI;
import Employees.Presentation.Utils.Option;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;

public class EmployeeChooseShiftsUI extends ViewCLI {
    private final ShiftsView shiftsView = new ShiftsView(0);
    private boolean open = false;
    private final EmployeeShiftController controller;
    private AvailabilitySubmissionPL availabilitySubmission;

    public EmployeeChooseShiftsUI(Runnable onDismiss) {
        super(onDismiss);
        controller = new EmployeeShiftController();
    }

    @Override
    public void display() {
        open = true;
        availabilitySubmission = new AvailabilitySubmissionPL();
        while (open) {
            System.out.println("Viewing Schedule of next week");
            shiftsView.display();
            System.out.println("X - Store Is Closed");
            System.out.println("U - Unsaved Changes");

            displayMenu(new Option.Builder("---Options---")
                    .append("Back (Cancels Changes)", onDismiss)
                    .append("Mark Shift as Unavailable", this::markShift)
                    .append("View Placement", this::viewShifts)
                    .append("Submit Availability", this::submit));
        }
    }

    private void viewShifts() {
        shiftsView.selectShift(shiftsView::displayShift);
    }

    private void markShift() {
        shiftsView.selectShift(((day, shift) -> {
            shiftsView.setUnsaved(day, shift);
            addSelectedShift(day, shift);
        }));
    }

    private void submit() {
        availabilitySubmission.setWorkingDoubles(
                getNextBoolean("Are you willing to work double shifts? (y/n)")
        );

        controller.setCurrentEmployeeShiftsAsUnavailable(availabilitySubmission);
        System.out.println("Availability Submitted");
        System.out.println("You can re-submit your availability again until the deadline: "
                           + SessionManager.getDeadline());
        onDismiss.run();
    }

    private void addSelectedShift(WeekDay day, ShiftType shift) {
        availabilitySubmission.setShift(new ShiftKey(day, shift), false);
    }

    @Override
    public void close() {
        open = false;
    }
}