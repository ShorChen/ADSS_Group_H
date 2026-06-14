package presentation.ui_manager;

import presentation.control.ViewBranchController;
import presentation.model.BranchPL;
import presentation.ui_shared.View;
import presentation.ui_shared.ViewCLI;
import presentation.util.Option;

public class ViewBranchUI extends ViewCLI {
    private ViewBranchController controller;
    private boolean open;

    public ViewBranchUI(BranchPL branch, Runnable onDismiss) {
        super(onDismiss);
        controller = new ViewBranchController(branch);
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            displayMenu(new Option.Builder(" --- Manage Branch --- ")
                    .append("Back", onDismiss)
                    .append("View Shift History", this::viewShiftHistory)
                    .append("Manage shifts", this::manageShifts)
                    .append("Manage Employees", this::manageEmployees)
            );
        }
    }

    private void manageEmployees() {
        ManageEmployeesUI manageEmployeesUI = new ManageEmployeesUI(this::display);
        close();
        manageEmployeesUI.display();
    }

    private void manageShifts() {
        ManageShiftsUI manageShiftsUI = new ManageShiftsUI(this::display);
        close();
        manageShiftsUI.display();
    }

    private void viewShiftHistory() {
        ViewShiftHistoryUI historyUI = new ViewShiftHistoryUI(this::display);
        close();
        historyUI.display();
    }

    @Override
    public void close() {
        open = false;
    }
}
