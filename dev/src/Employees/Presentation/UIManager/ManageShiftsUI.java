package Employees.Presentation.UIManager;

import Employees.Context.SessionManager;
import Employees.Presentation.Controller.HRManagerShiftController;
import Employees.Presentation.DTO.EmployeePL;
import Employees.Presentation.UIEmployee.RequestReplacementUI;
import Employees.Presentation.UIShared.ShiftsView;
import Employees.Presentation.UIShared.ViewCLI;
import Employees.Presentation.Utils.Option;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManageShiftsUI extends ViewCLI {
    private final HRManagerShiftController controller;
    private boolean open = false;
    private ShiftsView shiftsView;

    public ManageShiftsUI(Runnable onDismiss) {
        super(onDismiss);
        this.controller = new HRManagerShiftController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            shiftsView = new ShiftsView(0);
            shiftsView.display();
            displayMenu(new Option.Builder("--- Manage Shifts ---")
                    .append("Back", onDismiss)
                    .append("Open Shift", this::openShift)
                    .append("Close Shift", this::closeShift)
                    .append("Place Employee", this::placeEmployees)
                    .append("Set Submission Deadline", this::setDeadline)
                    .append("Handle Replacement Requests", this::handleReplacements)
                    .append("Issue HR Report (Not Implemented)", this::issueReport)

            );
        }
    }

    private void openShift() {
        shiftsView.selectClosedShifts(((day, type) -> {
            String id = getNextLine("Enter Shift Manager: ");
            try {
                EmployeePL shiftManager = controller.getShiftManager(id);
                controller.openShift(day, type, shiftManager);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }));
    }

    private void closeShift() {
        shiftsView.selectShift(controller::closeShift);
    }

    private void placeEmployees() {
        LocalDateTime deadline = SessionManager.getDeadline();
        LocalDateTime now = SessionManager.now();

        if (now.isAfter(deadline)) {
            PlaceEmployeesUI placeEmployeesUI = new PlaceEmployeesUI(this::display);
            close();
            placeEmployeesUI.display();
        } else {
            String deadlineString = deadline
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            System.out.println("Placements can only occur after the deadline");
            System.out.println("Come back at " + deadlineString);
        }

    }

    private void setDeadline() {
        boolean validDate = false;
        while (!validDate) {
            String date = getNextLine("Enter Date For Deadline (dd/MM/yyyy HH:mm) or type cancel to cancel: ");
            if (date.equalsIgnoreCase("cancel")) {
                System.out.println("Canceled");
                return;
            }
            try {
                controller.setDeadline(date);
                validDate = true;
                System.out.println("Deadline set to " + date);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void handleReplacements() {
        RequestReplacementUI requestUI = new RequestReplacementUI(this::display);
        close();
        requestUI.display();
    }

    private void issueReport() {
        System.out.println(controller.issueReport());
    }

    @Override
    public void close() {
        open = false;
    }
}