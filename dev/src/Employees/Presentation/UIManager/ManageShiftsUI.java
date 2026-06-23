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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ManageShiftsUI extends ViewCLI {
    private final HRManagerShiftController controller;
    private boolean open = false;
    private ShiftsView shiftsView;
    private boolean isFirstWeek;

    public ManageShiftsUI(Runnable onDismiss) {
        super(onDismiss);
        this.controller = new HRManagerShiftController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            isFirstWeek = controller.isFirstWeek();
            if (isFirstWeek) onFirstWeek();
            else shiftsView = new ShiftsView(0);

            if (open) {
                shiftsView.display();
                displayMenu(new Option.Builder("--- Manage Shifts ---")
                        .append("Back", onDismiss)
                        .append("Open Shift", this::openShift)
                        .append("Close Shift", this::closeShift)
                        .append("Place Employee", this::placeEmployees)
                        .append("Shift Staffing", this::shiftStaffing)
                );
            }
        }
    }

    private void onFirstWeek() {
        Option.Builder builder = new Option.Builder("Select Week");
        builder.append("Back", onDismiss);
        builder.append("This Week", () -> shiftsView = new ShiftsView(0));
        builder.append("Upcoming Week", () -> shiftsView = new ShiftsView(-1));
        displayMenu(builder);
    }

    private void shiftStaffing() {
        shiftsView.selectShift((day, type) -> {
            Map<String, Integer> staffing = new HashMap<>();
            AtomicBoolean keep = new AtomicBoolean(true);
            while (keep.get()) {
                displayMenu(new Option.Builder("")
                        .append("Save", () ->
                                controller.setShiftStaffing(day, type, staffing, isFirstWeek))
                        .append("Back (Cancels unsaved changes)", () -> keep.set(false))
                        .append("Set Staffing to Role", () -> selectRoleAndAmount(staffing))
                );
            }
        });
    }

    private void selectRoleAndAmount(Map<String, Integer> staffing) {
        String selectedRole = selectionMenuOf("Select Role", controller.getRoles());
        int amount = getNextInteger("Enter Amount (or -1 to cancel)");
        if (amount == -1)
            System.out.println("Canceled");
        else if (amount > 0)
            staffing.put(selectedRole, amount);
        else System.out.println("Amount Must be at least 1");
    }

    private void openShift() {
        shiftsView.selectClosedShifts(((day, type) -> {
            String id = getNextLine("Enter Shift Manager: ");
            try {
                EmployeePL shiftManager = controller.getShiftManager(id);
                controller.openShift(day, type, shiftManager, isFirstWeek);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }));
    }

    private void closeShift() {
        shiftsView.selectShift((day, type) ->
                controller.closeShift(day, type, isFirstWeek));
    }

    private void placeEmployees() {
        LocalDateTime deadline = SessionManager.getDeadline();
        LocalDateTime now = SessionManager.now();

        if (controller.isFirstWeek() || now.isAfter(deadline)) {

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

    @Override
    public void close() {
        open = false;
    }
}